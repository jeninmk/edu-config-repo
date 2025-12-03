#!/bin/bash

# Minikube Deployment Script for Education Platform
# Usage: ./deploy-minikube.sh [build|deploy|all|clean]

set -e

NAMESPACE="edu-platform"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log_info "Checking prerequisites..."
    
    if ! command -v minikube &> /dev/null; then
        log_error "minikube is not installed. Install with: brew install minikube"
        exit 1
    fi
    
    if ! command -v kubectl &> /dev/null; then
        log_error "kubectl is not installed. Install with: brew install kubectl"
        exit 1
    fi
    
    if ! command -v docker &> /dev/null; then
        log_error "docker is not installed. Install Docker Desktop or: brew install docker"
        exit 1
    fi
    
    log_info "All prerequisites met!"
}

# Start Minikube
start_minikube() {
    log_info "Starting Minikube..."
    
    if minikube status | grep -q "Running"; then
        log_info "Minikube is already running"
    else
        minikube start --cpus=2 --memory=4096 --driver=docker
    fi
    
    # Enable addons
    minikube addons enable ingress
    minikube addons enable metrics-server
    
    log_info "Minikube started successfully!"
}

# Configure Docker to use Minikube's daemon
configure_docker() {
    log_info "Configuring Docker to use Minikube's daemon..."
    eval $(minikube docker-env)
    log_info "Docker configured!"
}

# Build all Docker images
build_images() {
    log_info "Building Docker images..."
    
    configure_docker
    
    # Build Gateway Service
    log_info "Building gateway-service..."
    cd gateway-service
    ./mvnw clean package -DskipTests -q
    docker build -t gateway-service:latest .
    cd ..
    
    # Build Student Service
    log_info "Building student-service..."
    cd student-service
    ./mvnw clean package -DskipTests -q
    docker build -t student-service:latest .
    cd ..
    
    # Build Course Service
    log_info "Building course-service..."
    cd course-service
    ./mvnw clean package -DskipTests -q
    docker build -t course-service:latest .
    cd ..
    
    # Build Progress Service
    log_info "Building progress-service..."
    cd progress-service
    ./mvnw clean package -DskipTests -q
    docker build -t progress-service:latest .
    cd ..
    
    # Build Eureka Server (optional)
    log_info "Building eureka-server..."
    cd eureka-server
    ./mvnw clean package -DskipTests -q
    docker build -t eureka-server:latest .
    cd ..
    
    log_info "All images built successfully!"
}

# Create namespace
create_namespace() {
    log_info "Creating namespace $NAMESPACE..."
    kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -
    kubectl config set-context --current --namespace=$NAMESPACE
}

# Deploy all services
deploy_services() {
    log_info "Deploying services to Minikube..."
    
    create_namespace
    
    # Deploy secrets and configmaps first
    log_info "Deploying secrets and configmaps..."
    kubectl apply -f k8s/secrets.yaml
    kubectl apply -f k8s/configmap.yaml
    
    # Deploy PostgreSQL
    log_info "Deploying PostgreSQL..."
    kubectl apply -f k8s/postgres-deployment.yaml
    
    log_info "Waiting for PostgreSQL to be ready..."
    kubectl wait --for=condition=ready pod -l app=postgres --timeout=120s
    
    # Deploy Eureka (optional)
    log_info "Deploying Eureka Server (optional)..."
    kubectl apply -f k8s/eureka-deployment.yaml
    
    # Deploy Gateway Service
    log_info "Deploying Gateway Service..."
    kubectl apply -f k8s/gateway-deployment.yaml
    
    # Deploy Student Service
    log_info "Deploying Student Service..."
    kubectl apply -f k8s/student-deployment.yaml
    
    # Deploy Course Service
    log_info "Deploying Course Service..."
    kubectl apply -f k8s/course-deployment.yaml
    
    # Deploy Progress Service
    log_info "Deploying Progress Service..."
    kubectl apply -f k8s/progress-deployment.yaml
    
    log_info "All services deployed!"
    
    # Wait for services to be ready
    log_info "Waiting for services to be ready..."
    kubectl wait --for=condition=ready pod -l app=gateway-service --timeout=180s || log_warn "Gateway service not ready yet"
    kubectl wait --for=condition=ready pod -l app=student-service --timeout=180s || log_warn "Student service not ready yet"
    
    log_info "Deployment complete!"
}

# Show status
show_status() {
    log_info "Deployment Status:"
    echo ""
    echo "=== Pods ==="
    kubectl get pods -n $NAMESPACE
    echo ""
    echo "=== Services ==="
    kubectl get services -n $NAMESPACE
    echo ""
    echo "=== Gateway URL ==="
    minikube service gateway-service -n $NAMESPACE --url 2>/dev/null || echo "Service not ready yet"
}

# Clean up
cleanup() {
    log_info "Cleaning up..."
    kubectl delete namespace $NAMESPACE --ignore-not-found
    log_info "Cleanup complete!"
}

# Main
case "${1:-all}" in
    build)
        check_prerequisites
        start_minikube
        build_images
        ;;
    deploy)
        check_prerequisites
        deploy_services
        show_status
        ;;
    status)
        show_status
        ;;
    clean)
        cleanup
        ;;
    all)
        check_prerequisites
        start_minikube
        build_images
        deploy_services
        show_status
        ;;
    *)
        echo "Usage: $0 [build|deploy|status|clean|all]"
        echo ""
        echo "Commands:"
        echo "  build   - Build Docker images only"
        echo "  deploy  - Deploy to Minikube (assumes images are built)"
        echo "  status  - Show deployment status"
        echo "  clean   - Delete all resources"
        echo "  all     - Build and deploy (default)"
        exit 1
        ;;
esac
