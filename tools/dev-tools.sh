#!/bin/bash

# 芋道项目开发工具脚本
# 用法: ./dev-tools.sh [命令]

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 项目信息
PROJECT_NAME="芋道项目"
PROJECT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)
SERVER_DIR="$PROJECT_DIR/yudao-server"
TARGET_DIR="$SERVER_DIR/target"

# 打印带颜色的消息
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 显示帮助信息
show_help() {
    echo -e "${GREEN}$PROJECT_NAME 开发工具${NC}"
    echo
    echo "用法: $0 [命令]"
    echo
    echo "可用命令:"
    echo "  build           - 构建项目"
    echo "  run             - 运行项目"
    echo "  clean           - 清理项目"
    echo "  test            - 运行测试"
    echo "  package         - 打包项目"
    echo "  docker-build    - 构建Docker镜像"
    echo "  check-env       - 检查开发环境"
    echo "  init-db         - 初始化数据库"
    echo "  logs            - 查看应用日志"
    echo "  health          - 健康检查"
    echo "  restart         - 重启应用"
    echo "  help            - 显示此帮助信息"
    echo
    echo "示例:"
    echo "  $0 build        # 构建项目"
    echo "  $0 run          # 运行项目"
    echo "  $0 health       # 检查应用健康状态"
}

# 检查开发环境
check_env() {
    print_info "检查开发环境..."
    
    # 检查Java
    if command -v java &> /dev/null; then
        JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f 2)
        print_success "Java版本: $JAVA_VERSION"
    else
        print_error "Java未安装或未添加到PATH"
        return 1
    fi
    
    # 检查Maven
    if command -v mvn &> /dev/null; then
        MVN_VERSION=$(mvn -version | head -n 1 | cut -d' ' -f 3)
        print_success "Maven版本: $MVN_VERSION"
    else
        print_error "Maven未安装或未添加到PATH"
        return 1
    fi
    
    # 检查MySQL
    if command -v mysql &> /dev/null; then
        print_success "MySQL已安装"
    else
        print_warning "MySQL未找到，请确保已安装"
    fi
    
    # 检查Redis
    if command -v redis-cli &> /dev/null; then
        print_success "Redis已安装"
    else
        print_warning "Redis未找到，请确保已安装"
    fi
    
    # 检查端口占用
    if lsof -i :48080 &> /dev/null; then
        print_warning "端口48080已被占用"
    else
        print_success "端口48080可用"
    fi
    
    print_success "环境检查完成"
}

# 构建项目
build_project() {
    print_info "开始构建项目..."
    cd "$PROJECT_DIR"
    
    if mvn clean compile -Dmaven.test.skip=true; then
        print_success "项目构建成功"
    else
        print_error "项目构建失败"
        return 1
    fi
}

# 运行项目
run_project() {
    print_info "启动项目..."
    cd "$SERVER_DIR"
    
    # 检查是否已构建
    if [ ! -f "target/yudao-server.jar" ]; then
        print_info "JAR文件不存在，开始构建..."
        cd "$PROJECT_DIR"
        mvn clean package -Dmaven.test.skip=true
    fi
    
    cd "$SERVER_DIR"
    print_info "启动应用..."
    
    # 设置JVM参数
    JAVA_OPTS="-Xms512m -Xmx1g -XX:+UseG1GC"
    APP_OPTS="--spring.profiles.active=local"
    
    java $JAVA_OPTS -jar target/yudao-server.jar $APP_OPTS
}

# 清理项目
clean_project() {
    print_info "清理项目..."
    cd "$PROJECT_DIR"
    
    if mvn clean; then
        print_success "项目清理完成"
    else
        print_error "项目清理失败"
        return 1
    fi
}

# 运行测试
run_tests() {
    print_info "运行测试..."
    cd "$PROJECT_DIR"
    
    if mvn test; then
        print_success "测试运行完成"
    else
        print_error "测试运行失败"
        return 1
    fi
}

# 打包项目
package_project() {
    print_info "打包项目..."
    cd "$PROJECT_DIR"
    
    if mvn clean package -Dmaven.test.skip=true; then
        print_success "项目打包完成"
        echo "JAR文件位置: $TARGET_DIR/yudao-server.jar"
    else
        print_error "项目打包失败"
        return 1
    fi
}

# 构建Docker镜像
docker_build() {
    print_info "构建Docker镜像..."
    
    # 先打包项目
    package_project
    
    cd "$SERVER_DIR"
    if docker build -t yudao-server:latest .; then
        print_success "Docker镜像构建完成"
        docker images | grep yudao-server
    else
        print_error "Docker镜像构建失败"
        return 1
    fi
}

# 初始化数据库
init_database() {
    print_info "初始化数据库..."
    
    read -p "请输入MySQL用户名 [root]: " DB_USER
    DB_USER=${DB_USER:-root}
    
    read -s -p "请输入MySQL密码: " DB_PASSWORD
    echo
    
    read -p "请输入数据库名 [ruoyi_vue_pro]: " DB_NAME
    DB_NAME=${DB_NAME:-ruoyi_vue_pro}
    
    # 创建数据库
    mysql -u"$DB_USER" -p"$DB_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS $DB_NAME DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
    
    # 导入SQL文件
    if [ -f "$PROJECT_DIR/sql/mysql/ruoyi-vue-pro.sql" ]; then
        mysql -u"$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$PROJECT_DIR/sql/mysql/ruoyi-vue-pro.sql"
        print_success "数据库初始化完成"
    else
        print_error "SQL文件不存在: $PROJECT_DIR/sql/mysql/ruoyi-vue-pro.sql"
        return 1
    fi
}

# 查看日志
view_logs() {
    LOG_FILE="$SERVER_DIR/logs/app.log"
    
    if [ -f "$LOG_FILE" ]; then
        print_info "查看应用日志 (Ctrl+C 退出)..."
        tail -f "$LOG_FILE"
    else
        print_warning "日志文件不存在: $LOG_FILE"
        print_info "尝试查看Maven运行日志..."
        find "$PROJECT_DIR" -name "*.log" -type f -exec tail -f {} +
    fi
}

# 健康检查
health_check() {
    print_info "执行健康检查..."
    
    # 检查端口
    if lsof -i :48080 &> /dev/null; then
        print_success "应用端口48080正在监听"
        
        # 检查应用状态
        if curl -s http://localhost:48080/actuator/health &> /dev/null; then
            print_success "应用健康状态正常"
            print_info "访问地址:"
            echo "  - 后端API: http://localhost:48080"
            echo "  - 接口文档: http://localhost:48080/doc.html"
        else
            print_warning "应用健康检查失败"
        fi
    else
        print_error "应用未启动或端口48080未监听"
    fi
}

# 重启应用
restart_app() {
    print_info "重启应用..."
    
    # 查找Java进程
    PID=$(jps | grep yudao-server | awk '{print $1}')
    
    if [ -n "$PID" ]; then
        print_info "停止应用 (PID: $PID)..."
        kill "$PID"
        sleep 3
    fi
    
    print_info "启动应用..."
    run_project
}

# 主函数
main() {
    case "$1" in
        build)
            build_project
            ;;
        run)
            run_project
            ;;
        clean)
            clean_project
            ;;
        test)
            run_tests
            ;;
        package)
            package_project
            ;;
        docker-build)
            docker_build
            ;;
        check-env)
            check_env
            ;;
        init-db)
            init_database
            ;;
        logs)
            view_logs
            ;;
        health)
            health_check
            ;;
        restart)
            restart_app
            ;;
        help|--help|-h)
            show_help
            ;;
        "")
            show_help
            ;;
        *)
            print_error "未知命令: $1"
            echo
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"