// Custom JavaScript for Tech Mahindra RMS

// Initialize application when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
});

function initializeApp() {
    // Initialize tooltips
    initializeTooltips();
    
    // Initialize confirmations
    initializeConfirmations();
    
    // Initialize form validations
    initializeFormValidations();
    
    // Initialize search functionality
    initializeSearch();
    
    // Initialize auto-refresh for dashboard
    initializeAutoRefresh();
}

// Initialize Bootstrap tooltips
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// Initialize confirmation dialogs
function initializeConfirmations() {
    // Delete confirmations
    document.querySelectorAll('[data-confirm]').forEach(function(element) {
        element.addEventListener('click', function(e) {
            const message = this.getAttribute('data-confirm') || 'Are you sure you want to delete this item?';
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });
}

// Initialize form validations
function initializeFormValidations() {
    // Bootstrap validation
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });
    
    // Email validation
    document.querySelectorAll('input[type="email"]').forEach(function(input) {
        input.addEventListener('blur', function() {
            validateEmail(this);
        });
    });
    
    // Phone validation
    document.querySelectorAll('input[data-type="phone"]').forEach(function(input) {
        input.addEventListener('blur', function() {
            validatePhone(this);
        });
    });
}

// Initialize search functionality
function initializeSearch() {
    const searchInputs = document.querySelectorAll('.search-input');
    searchInputs.forEach(function(input) {
        let searchTimeout;
        input.addEventListener('input', function() {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(() => {
                performSearch(this.value);
            }, 500);
        });
    });
}

// Initialize auto-refresh for dashboard
function initializeAutoRefresh() {
    if (window.location.pathname.includes('/dashboard')) {
        // Refresh dashboard data every 5 minutes
        setInterval(function() {
            refreshDashboardData();
        }, 300000);
    }
}

// Utility Functions

// Email validation
function validateEmail(input) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const isValid = emailRegex.test(input.value);
    
    if (input.value && !isValid) {
        input.classList.add('is-invalid');
        showFieldError(input, 'Please enter a valid email address');
    } else {
        input.classList.remove('is-invalid');
        hideFieldError(input);
    }
    
    return isValid;
}

// Phone validation
function validatePhone(input) {
    const phoneRegex = /^[+]?[0-9]{10,13}$/;
    const isValid = phoneRegex.test(input.value.replace(/\D/g, ''));
    
    if (input.value && !isValid) {
        input.classList.add('is-invalid');
        showFieldError(input, 'Please enter a valid phone number');
    } else {
        input.classList.remove('is-invalid');
        hideFieldError(input);
    }
    
    return isValid;
}

// Show field error
function showFieldError(input, message) {
    hideFieldError(input);
    const errorDiv = document.createElement('div');
    errorDiv.className = 'invalid-feedback';
    errorDiv.textContent = message;
    input.parentNode.appendChild(errorDiv);
}

// Hide field error
function hideFieldError(input) {
    const errorDiv = input.parentNode.querySelector('.invalid-feedback');
    if (errorDiv) {
        errorDiv.remove();
    }
}

// Perform search
function performSearch(query) {
    // This function would be implemented to perform AJAX search
    console.log('Searching for:', query);
}

// Refresh dashboard data
function refreshDashboardData() {
    fetch('/api/dashboard/data')
        .then(response => response.json())
        .then(data => {
            updateDashboardMetrics(data);
        })
        .catch(error => {
            console.error('Error refreshing dashboard data:', error);
        });
}

// Update dashboard metrics
function updateDashboardMetrics(data) {
    // Update metric cards
    updateMetric('totalEmployees', data.totalEmployees);
    updateMetric('availableResources', data.availableResources);
    updateMetric('activeProjects', data.activeProjects);
    updateMetric('activeAllocations', data.activeAllocations);
}

// Update individual metric
function updateMetric(metricName, value) {
    const element = document.querySelector(`[data-metric="${metricName}"]`);
    if (element) {
        element.textContent = value;
    }
}

// Show loading spinner
function showLoading(element) {
    const spinner = document.createElement('div');
    spinner.className = 'spinner me-2';
    element.insertBefore(spinner, element.firstChild);
    element.disabled = true;
}

// Hide loading spinner
function hideLoading(element) {
    const spinner = element.querySelector('.spinner');
    if (spinner) {
        spinner.remove();
    }
    element.disabled = false;
}

// Show toast notification
function showToast(message, type = 'info') {
    const toastContainer = getOrCreateToastContainer();
    const toast = createToast(message, type);
    toastContainer.appendChild(toast);
    
    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();
    
    // Remove toast after it's hidden
    toast.addEventListener('hidden.bs.toast', function() {
        toast.remove();
    });
}

// Get or create toast container
function getOrCreateToastContainer() {
    let container = document.querySelector('.toast-container');
    if (!container) {
        container = document.createElement('div');
        container.className = 'toast-container position-fixed top-0 end-0 p-3';
        document.body.appendChild(container);
    }
    return container;
}

// Create toast element
function createToast(message, type) {
    const toast = document.createElement('div');
    toast.className = `toast align-items-center text-white bg-${type} border-0`;
    toast.setAttribute('role', 'alert');
    toast.setAttribute('aria-live', 'assertive');
    toast.setAttribute('aria-atomic', 'true');
    
    toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">
                ${message}
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `;
    
    return toast;
}

// Format date
function formatDate(date) {
    return new Date(date).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR'
    }).format(amount);
}

// Export functions for global use
window.TechMahindraRMS = {
    showToast,
    showLoading,
    hideLoading,
    formatDate,
    formatCurrency,
    validateEmail,
    validatePhone
};