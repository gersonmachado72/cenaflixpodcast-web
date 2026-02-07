/**
 * Cenaflix Podcast - JavaScript Principal
 */

// Configurações globais
const CONFIG = {
    API_BASE_URL: '/api/v1',
    TOKEN_KEY: 'cenaflix_token',
    USER_KEY: 'cenaflix_user'
};

// Utilitários
const Utils = {
    // Mostrar mensagem de alerta
    showAlert: function(message, type = 'info') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type}`;
        alertDiv.innerHTML = `
            <strong>${type.toUpperCase()}:</strong> ${message}
            <button type="button" class="close" onclick="this.parentElement.remove()">
                &times;
            </button>
        `;
        
        const container = document.querySelector('.container') || document.body;
        container.insertBefore(alertDiv, container.firstChild);
        
        setTimeout(() => {
            if (alertDiv.parentElement) {
                alertDiv.remove();
            }
        }, 5000);
    },
    
    // Formatar data
    formatDate: function(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('pt-BR') + ' ' + 
               date.toLocaleTimeString('pt-BR');
    },
    
    // Formatar duração
    formatDuration: function(duration) {
        if (!duration) return '00:00';
        if (duration.includes(':')) return duration;
        const mins = Math.floor(duration / 60);
        const secs = duration % 60;
        return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
    },
    
    // Validar formulário
    validateForm: function(formId) {
        const form = document.getElementById(formId);
        if (!form) return true;
        
        const inputs = form.querySelectorAll('[required]');
        let isValid = true;
        
        inputs.forEach(input => {
            if (!input.value.trim()) {
                input.classList.add('is-invalid');
                isValid = false;
            } else {
                input.classList.remove('is-invalid');
            }
        });
        
        return isValid;
    },
    
    // Carregar com AJAX
    ajaxRequest: async function(url, method = 'GET', data = null) {
        const token = localStorage.getItem(CONFIG.TOKEN_KEY);
        const headers = {
            'Content-Type': 'application/json'
        };
        
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        
        const options = {
            method,
            headers
        };
        
        if (data && (method === 'POST' || method === 'PUT')) {
            options.body = JSON.stringify(data);
        }
        
        try {
            const response = await fetch(url, options);
            
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Erro na requisição:', error);
            this.showAlert(`Erro: ${error.message}`, 'error');
            throw error;
        }
    },
    
    // Upload de arquivo
    uploadFile: async function(url, file, onProgress = null) {
        const formData = new FormData();
        formData.append('file', file);
        
        const token = localStorage.getItem(CONFIG.TOKEN_KEY);
        const headers = {};
        
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        
        return new Promise((resolve, reject) => {
            const xhr = new XMLHttpRequest();
            
            if (onProgress) {
                xhr.upload.addEventListener('progress', onProgress);
            }
            
            xhr.addEventListener('load', () => {
                if (xhr.status === 200) {
                    resolve(JSON.parse(xhr.responseText));
                } else {
                    reject(new Error(`Upload failed: ${xhr.statusText}`));
                }
            });
            
            xhr.addEventListener('error', () => {
                reject(new Error('Network error during upload'));
            });
            
            xhr.open('POST', url);
            if (token) {
                xhr.setRequestHeader('Authorization', `Bearer ${token}`);
            }
            xhr.send(formData);
        });
    }
};

// Gerenciamento de autenticação
const Auth = {
    isAuthenticated: function() {
        return !!localStorage.getItem(CONFIG.TOKEN_KEY);
    },
    
    getCurrentUser: function() {
        const userStr = localStorage.getItem(CONFIG.USER_KEY);
        return userStr ? JSON.parse(userStr) : null;
    },
    
    login: async function(username, password) {
        try {
            const response = await Utils.ajaxRequest('/api/auth/login', 'POST', {
                username,
                password
            });
            
            localStorage.setItem(CONFIG.TOKEN_KEY, response.accessToken);
            localStorage.setItem(CONFIG.USER_KEY, JSON.stringify({
                username: response.username,
                authorities: response.authorities
            }));
            
            return true;
        } catch (error) {
            console.error('Login failed:', error);
            return false;
        }
    },
    
    logout: function() {
        localStorage.removeItem(CONFIG.TOKEN_KEY);
        localStorage.removeItem(CONFIG.USER_KEY);
        window.location.href = '/login';
    },
    
    hasRole: function(role) {
        const user = this.getCurrentUser();
        if (!user || !user.authorities) return false;
        
        return user.authorities.some(auth => 
            auth.authority === `ROLE_${role.toUpperCase()}`);
    }
};

// Gerenciamento de podcasts
const PodcastManager = {
    loadPodcasts: async function(page = 0, size = 10) {
        try {
            const data = await Utils.ajaxRequest(
                `${CONFIG.API_BASE_URL}/podcasts?page=${page}&size=${size}`
            );
            
            return data;
        } catch (error) {
            console.error('Erro ao carregar podcasts:', error);
            return { content: [], totalElements: 0 };
        }
    },
    
    searchByProducer: async function(producer) {
        try {
            return await Utils.ajaxRequest(
                `${CONFIG.API_BASE_URL}/podcasts/search?produtor=${encodeURIComponent(producer)}`
            );
        } catch (error) {
            console.error('Erro na busca:', error);
            return [];
        }
    },
    
    deletePodcast: async function(id) {
        if (!confirm('Tem certeza que deseja excluir este podcast?')) {
            return false;
        }
        
        try {
            await Utils.ajaxRequest(
                `${CONFIG.API_BASE_URL}/podcasts/${id}`,
                'DELETE'
            );
            
            Utils.showAlert('Podcast excluído com sucesso!', 'success');
            return true;
        } catch (error) {
            Utils.showAlert('Erro ao excluir podcast', 'error');
            return false;
        }
    },
    
    createOrUpdate: async function(podcastData, id = null) {
        const url = id ? 
            `${CONFIG.API_BASE_URL}/podcasts/${id}` : 
            `${CONFIG.API_BASE_URL}/podcasts`;
        const method = id ? 'PUT' : 'POST';
        
        try {
            const result = await Utils.ajaxRequest(url, method, podcastData);
            Utils.showAlert(
                `Podcast ${id ? 'atualizado' : 'criado'} com sucesso!`,
                'success'
            );
            return result;
        } catch (error) {
            Utils.showAlert(
                `Erro ao ${id ? 'atualizar' : 'criar'} podcast`,
                'error'
            );
            throw error;
        }
    }
};

// Upload de arquivos
const FileUploader = {
    init: function(dropZoneId, fileInputId, previewId) {
        const dropZone = document.getElementById(dropZoneId);
        const fileInput = document.getElementById(fileInputId);
        const preview = document.getElementById(previewId);
        
        if (!dropZone || !fileInput) return;
        
        // Click no dropzone abre o file input
        dropZone.addEventListener('click', () => fileInput.click());
        
        // Arrastar e soltar
        dropZone.addEventListener('dragover', (e) => {
            e.preventDefault();
            dropZone.classList.add('dragover');
        });
        
        dropZone.addEventListener('dragleave', () => {
            dropZone.classList.remove('dragover');
        });
        
        dropZone.addEventListener('drop', (e) => {
            e.preventDefault();
            dropZone.classList.remove('dragover');
            
            if (e.dataTransfer.files.length) {
                fileInput.files = e.dataTransfer.files;
                this.handleFiles(fileInput.files, preview);
            }
        });
        
        // Seleção pelo input
        fileInput.addEventListener('change', () => {
            this.handleFiles(fileInput.files, preview);
        });
    },
    
    handleFiles: function(files, previewElement) {
        if (!previewElement) return;
        
        previewElement.innerHTML = '';
        
        Array.from(files).forEach(file => {
            if (!file.type.match('image.*')) return;
            
            const reader = new FileReader();
            
            reader.onload = (e) => {
                const img = document.createElement('img');
                img.src = e.target.result;
                img.style.maxWidth = '200px';
                img.style.maxHeight = '200px';
                img.style.margin = '10px';
                previewElement.appendChild(img);
            };
            
            reader.readAsDataURL(file);
        });
    },
    
    upload: async function(podcastId, file, onProgress) {
        try {
            return await Utils.uploadFile(
                `/api/files/upload/${podcastId}`,
                file,
                onProgress
            );
        } catch (error) {
            console.error('Upload failed:', error);
            throw error;
        }
    }
};

// Relatórios e gráficos
const Reports = {
    initCharts: function() {
        // Exemplo: Gráfico de produtores
        const producerCtx = document.getElementById('producerChart');
        if (producerCtx) {
            this.createProducerChart(producerCtx);
        }
        
        // Exemplo: Gráfico mensal
        const monthlyCtx = document.getElementById('monthlyChart');
        if (monthlyCtx) {
            this.createMonthlyChart(monthlyCtx);
        }
    },
    
    createProducerChart: function(ctx) {
        // Dados seriam carregados via AJAX
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Produtor 1', 'Produtor 2', 'Produtor 3'],
                datasets: [{
                    label: 'Podcasts por Produtor',
                    data: [12, 19, 8],
                    backgroundColor: 'rgba(102, 126, 234, 0.6)'
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    },
    
    createMonthlyChart: function(ctx) {
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun'],
                datasets: [{
                    label: 'Podcasts por Mês',
                    data: [12, 19, 3, 5, 2, 3],
                    borderColor: 'rgba(76, 175, 80, 1)',
                    backgroundColor: 'rgba(76, 175, 80, 0.2)',
                    fill: true
                }]
            },
            options: {
                responsive: true
            }
        });
    },
    
    loadStats: async function() {
        try {
            return await Utils.ajaxRequest(`${CONFIG.API_BASE_URL}/podcasts/stats`);
        } catch (error) {
            console.error('Erro ao carregar estatísticas:', error);
            return null;
        }
    }
};

// Inicialização quando a página carrega
document.addEventListener('DOMContentLoaded', function() {
    // Inicializar componentes
    if (typeof FileUploader !== 'undefined') {
        FileUploader.init('dropZone', 'fileInput', 'preview');
    }
    
    if (typeof Reports !== 'undefined') {
        Reports.initCharts();
    }
    
    // Configurar formulários
    const forms = document.querySelectorAll('form[data-validate="true"]');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!Utils.validateForm(this.id)) {
                e.preventDefault();
                Utils.showAlert('Por favor, preencha todos os campos obrigatórios.', 'error');
            }
        });
    });
    
    // Configurar tooltips
    const tooltips = document.querySelectorAll('[title]');
    tooltips.forEach(el => {
        el.setAttribute('data-toggle', 'tooltip');
    });
    
    // Verificar autenticação para páginas protegidas
    if (!Auth.isAuthenticated() && 
        !window.location.pathname.includes('/login') &&
        !window.location.pathname.includes('/public')) {
        Auth.logout();
    }
    
    // Mostrar/ocultar elementos baseado em roles
    const roleElements = document.querySelectorAll('[data-role]');
    roleElements.forEach(el => {
        const requiredRole = el.getAttribute('data-role');
        if (!Auth.hasRole(requiredRole)) {
            el.style.display = 'none';
        }
    });
});

// Exportar para uso global
window.Utils = Utils;
window.Auth = Auth;
window.PodcastManager = PodcastManager;
window.FileUploader = FileUploader;
window.Reports = Reports;
