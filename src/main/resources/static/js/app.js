const API_BASE = 'http://localhost:8080/api';

let currentUser = null;
let currentPage = 'dashboard';

// ==================== API Helper ====================
async function apiCall(endpoint, method = 'GET', body = null) {
    const options = {
        method,
        headers: { 'Content-Type': 'application/json' }
    };
    if (body) options.body = JSON.stringify(body);

    const response = await fetch(`${API_BASE}${endpoint}`, options);
    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || 'An error occurred');
    }
    return data;
}

// ==================== Toast Notification ====================
function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type}`;
    setTimeout(() => toast.classList.add('hidden'), 3000);
}

// ==================== Modal ====================
function openModal(title, content) {
    document.getElementById('modalTitle').textContent = title;
    document.getElementById('modalBody').innerHTML = content;
    document.getElementById('modal').classList.remove('hidden');
}

function closeModal() {
    document.getElementById('modal').classList.add('hidden');
}

// ==================== Auth ====================
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const result = await apiCall('/auth/login', 'POST', { username, password });
        currentUser = result.data;
        document.getElementById('loginPage').classList.add('hidden');
        document.getElementById('appContainer').classList.remove('hidden');
        document.getElementById('userRole').textContent = currentUser.role;
        loadDashboard();
        showToast('Login successful');
    } catch (error) {
        document.getElementById('loginError').textContent = error.message;
    }
});

document.getElementById('logoutBtn').addEventListener('click', () => {
    currentUser = null;
    document.getElementById('appContainer').classList.add('hidden');
    document.getElementById('loginPage').classList.remove('hidden');
    document.getElementById('username').value = '';
    document.getElementById('password').value = '';
});

// ==================== Navigation ====================
document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', (e) => {
        e.preventDefault();
        document.querySelectorAll('.nav-item').forEach(i => i.classList.remove('active'));
        item.classList.add('active');

        const page = item.dataset.page;
        document.querySelectorAll('.content-page').forEach(p => p.classList.remove('active'));
        document.getElementById(`${page}Page`).classList.add('active');
        currentPage = page;

        loadPageData(page);
    });
});

function loadPageData(page) {
    switch (page) {
        case 'dashboard': loadDashboard(); break;
        case 'employees': loadEmployees(); break;
        case 'departments': loadDepartments(); break;
        case 'attendance': loadAttendance(); break;
        case 'leaves': loadLeaves(); break;
        case 'payroll': loadPayroll(); break;
        case 'users': loadUsers(); break;
    }
}

// ==================== Dashboard ====================
async function loadDashboard() {
    try {
        const [empRes, deptRes, leaveRes] = await Promise.all([
            apiCall('/employees'),
            apiCall('/departments'),
            apiCall('/leaves/pending')
        ]);
        document.getElementById('totalEmployees').textContent = empRes.data.length;
        document.getElementById('totalDepartments').textContent = deptRes.data.length;
        document.getElementById('pendingLeaves').textContent = leaveRes.data.length;
    } catch (error) {
        showToast('Error loading dashboard', 'error');
    }
}

// ==================== Employees ====================
async function loadEmployees() {
    try {
        const result = await apiCall('/employees');
        renderEmployeeTable(result.data);
    } catch (error) {
        showToast('Error loading employees', 'error');
    }
}

function renderEmployeeTable(employees) {
    const tbody = document.getElementById('employeeTableBody');
    tbody.innerHTML = employees.map(emp => `
        <tr>
            <td>${emp.empId}</td>
            <td>${emp.firstName} ${emp.lastName}</td>
            <td>${emp.email}</td>
            <td>${emp.phone || '-'}</td>
            <td>${emp.departmentName || '-'}</td>
            <td>${emp.hireDate}</td>
            <td class="${emp.isActive ? 'status-active' : 'status-inactive'}">
                ${emp.isActive ? 'Active' : 'Inactive'}
            </td>
            <td class="actions">
                <button class="btn btn-sm btn-secondary" onclick="editEmployee(${emp.empId})">Edit</button>
                <button class="btn btn-sm ${emp.isActive ? 'btn-warning' : 'btn-success'}" 
                        onclick="toggleEmployeeStatus(${emp.empId})">
                    ${emp.isActive ? 'Deactivate' : 'Activate'}
                </button>
            </td>
        </tr>
    `).join('');
}

document.getElementById('addEmployeeBtn').addEventListener('click', () => {
    openModal('Add Employee', `
        <form id="employeeForm">
            <div class="form-group">
                <label>First Name</label>
                <input type="text" id="empFirstName" required>
            </div>
            <div class="form-group">
                <label>Last Name</label>
                <input type="text" id="empLastName" required>
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" id="empEmail" required>
            </div>
            <div class="form-group">
                <label>Phone</label>
                <input type="text" id="empPhone">
            </div>
            <div class="form-group">
                <label>Hire Date</label>
                <input type="date" id="empHireDate" required>
            </div>
            <div class="form-group">
                <label>Department</label>
                <select id="empDept"></select>
            </div>
            <button type="submit" class="btn btn-primary">Save</button>
        </form>
    `);
    loadDepartmentDropdown();
    document.getElementById('employeeForm').addEventListener('submit', saveEmployee);
});

async function loadDepartmentDropdown() {
    try {
        const result = await apiCall('/departments');
        const select = document.getElementById('empDept');
        select.innerHTML = '<option value="">Select Department</option>' +
            result.data.map(d => `<option value="${d.deptId}">${d.deptName}</option>`).join('');
    } catch (error) {
        showToast('Error loading departments', 'error');
    }
}

async function saveEmployee(e) {
    e.preventDefault();
    try {
        await apiCall('/employees', 'POST', {
            firstName: document.getElementById('empFirstName').value,
            lastName: document.getElementById('empLastName').value,
            email: document.getElementById('empEmail').value,
            phone: document.getElementById('empPhone').value,
            hireDate: document.getElementById('empHireDate').value,
            deptId: document.getElementById('empDept').value || null
        });
        closeModal();
        loadEmployees();
        showToast('Employee created successfully');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function editEmployee(empId) {
    try {
        const result = await apiCall(`/employees/${empId}`);
        const emp = result.data;
        openModal('Edit Employee', `
            <form id="editEmployeeForm">
                <div class="form-group">
                    <label>First Name</label>
                    <input type="text" id="editEmpFirstName" value="${emp.firstName}" required>
                </div>
                <div class="form-group">
                    <label>Last Name</label>
                    <input type="text" id="editEmpLastName" value="${emp.lastName}" required>
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" id="editEmpEmail" value="${emp.email}" required>
                </div>
                <div class="form-group">
                    <label>Phone</label>
                    <input type="text" id="editEmpPhone" value="${emp.phone || ''}">
                </div>
                <div class="form-group">
                    <label>Hire Date</label>
                    <input type="date" id="editEmpHireDate" value="${emp.hireDate}" required>
                </div>
                <div class="form-group">
                    <label>Department</label>
                    <select id="editEmpDept"></select>
                </div>
                <button type="submit" class="btn btn-primary">Update</button>
            </form>
        `);
        const deptRes = await apiCall('/departments');
        const select = document.getElementById('editEmpDept');
        select.innerHTML = '<option value="">Select Department</option>' +
            deptRes.data.map(d => `<option value="${d.deptId}" ${d.deptId === emp.deptId ? 'selected' : ''}>${d.deptName}</option>`).join('');
        document.getElementById('editEmployeeForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            try {
                await apiCall(`/employees/${empId}`, 'PUT', {
                    firstName: document.getElementById('editEmpFirstName').value,
                    lastName: document.getElementById('editEmpLastName').value,
                    email: document.getElementById('editEmpEmail').value,
                    phone: document.getElementById('editEmpPhone').value,
                    hireDate: document.getElementById('editEmpHireDate').value,
                    deptId: document.getElementById('editEmpDept').value || null
                });
                closeModal();
                loadEmployees();
                showToast('Employee updated successfully');
            } catch (error) {
                showToast(error.message, 'error');
            }
        });
    } catch (error) {
        showToast('Error loading employee', 'error');
    }
}

async function toggleEmployeeStatus(empId) {
    try {
        await apiCall(`/employees/${empId}/toggle-status`, 'PATCH');
        loadEmployees();
        showToast('Status updated');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

document.getElementById('employeeSearch').addEventListener('input', async (e) => {
    const keyword = e.target.value;
    if (keyword.length < 2) {
        loadEmployees();
        return;
    }
    try {
        const result = await apiCall(`/employees/search?keyword=${keyword}`);
        renderEmployeeTable(result.data);
    } catch (error) {
        showToast('Error searching employees', 'error');
    }
});

// ==================== Departments ====================
async function loadDepartments() {
    try {
        const result = await apiCall('/departments');
        renderDepartmentTable(result.data);
    } catch (error) {
        showToast('Error loading departments', 'error');
    }
}

function renderDepartmentTable(departments) {
    const tbody = document.getElementById('departmentTableBody');
    tbody.innerHTML = departments.map(dept => `
        <tr>
            <td>${dept.deptId}</td>
            <td>${dept.deptName}</td>
            <td>${dept.location || '-'}</td>
            <td>${dept.employeeCount}</td>
            <td class="actions">
                <button class="btn btn-sm btn-secondary" onclick="editDepartment(${dept.deptId})">Edit</button>
                <button class="btn btn-sm btn-danger" onclick="deleteDepartment(${dept.deptId})">Delete</button>
            </td>
        </tr>
    `).join('');
}

document.getElementById('addDepartmentBtn').addEventListener('click', () => {
    openModal('Add Department', `
        <form id="departmentForm">
            <div class="form-group">
                <label>Department Name</label>
                <input type="text" id="deptName" required>
            </div>
            <div class="form-group">
                <label>Location</label>
                <input type="text" id="deptLocation">
            </div>
            <button type="submit" class="btn btn-primary">Save</button>
        </form>
    `);
    document.getElementById('departmentForm').addEventListener('submit', saveDepartment);
});

async function saveDepartment(e) {
    e.preventDefault();
    try {
        await apiCall('/departments', 'POST', {
            deptName: document.getElementById('deptName').value,
            location: document.getElementById('deptLocation').value
        });
        closeModal();
        loadDepartments();
        showToast('Department created successfully');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function deleteDepartment(deptId) {
    if (!confirm('Are you sure you want to delete this department?')) return;
    try {
        await apiCall(`/departments/${deptId}`, 'DELETE');
        loadDepartments();
        showToast('Department deleted');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function editDepartment(deptId) {
    try {
        const result = await apiCall(`/departments/${deptId}`);
        const dept = result.data;
        openModal('Edit Department', `
            <form id="editDepartmentForm">
                <div class="form-group">
                    <label>Department Name</label>
                    <input type="text" id="editDeptName" value="${dept.deptName}" required>
                </div>
                <div class="form-group">
                    <label>Location</label>
                    <input type="text" id="editDeptLocation" value="${dept.location || ''}">
                </div>
                <button type="submit" class="btn btn-primary">Update</button>
            </form>
        `);
        document.getElementById('editDepartmentForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            try {
                await apiCall(`/departments/${deptId}`, 'PUT', {
                    deptName: document.getElementById('editDeptName').value,
                    location: document.getElementById('editDeptLocation').value
                });
                closeModal();
                loadDepartments();
                showToast('Department updated');
            } catch (error) {
                showToast(error.message, 'error');
            }
        });
    } catch (error) {
        showToast('Error loading department', 'error');
    }
}

// ==================== Attendance ====================
async function loadAttendance() {
    if (!currentUser || !currentUser.empId) return;
    try {
        const result = await apiCall(`/attendance/today/${currentUser.empId}`);
        if (result.data) {
            document.getElementById('attendanceStatus').textContent = result.data.status;
            document.getElementById('checkInTime').textContent = result.data.checkIn || '--:--';
            document.getElementById('checkOutTime').textContent = result.data.checkOut || '--:--';
            document.getElementById('hoursWorked').textContent = result.data.hoursWorked || '0';
        }
    } catch (error) {
        showToast('Error loading attendance', 'error');
    }
}

document.getElementById('checkInBtn').addEventListener('click', async () => {
    if (!currentUser || !currentUser.empId) {
        showToast('No employee linked to your account', 'error');
        return;
    }
    try {
        await apiCall(`/attendance/check-in/${currentUser.empId}`, 'POST');
        loadAttendance();
        showToast('Checked in successfully');
    } catch (error) {
        showToast(error.message, 'error');
    }
});

document.getElementById('checkOutBtn').addEventListener('click', async () => {
    if (!currentUser || !currentUser.empId) {
        showToast('No employee linked to your account', 'error');
        return;
    }
    try {
        await apiCall(`/attendance/check-out/${currentUser.empId}`, 'POST');
        loadAttendance();
        showToast('Checked out successfully');
    } catch (error) {
        showToast(error.message, 'error');
    }
});

document.getElementById('filterAttendanceBtn').addEventListener('click', async () => {
    if (!currentUser || !currentUser.empId) return;
    const start = document.getElementById('attStartDate').value;
    const end = document.getElementById('attEndDate').value;
    if (!start || !end) {
        showToast('Please select both dates', 'error');
        return;
    }
    try {
        const result = await apiCall(`/attendance/employee/${currentUser.empId}?start=${start}&end=${end}`);
        const tbody = document.getElementById('attendanceTableBody');
        tbody.innerHTML = result.data.map(att => `
            <tr>
                <td>${att.attDate}</td>
                <td>${att.checkIn || '-'}</td>
                <td>${att.checkOut || '-'}</td>
                <td>${att.hoursWorked || '0'}</td>
                <td>${att.status}</td>
            </tr>
        `).join('');
    } catch (error) {
        showToast('Error loading attendance', 'error');
    }
});

// ==================== Leaves ====================
async function loadLeaves() {
    if (!currentUser || !currentUser.empId) return;
    try {
        const [myLeaves, pendingLeaves] = await Promise.all([
            apiCall(`/leaves/employee/${currentUser.empId}`),
            apiCall('/leaves/pending')
        ]);
        renderMyLeaves(myLeaves.data);
        renderPendingLeaves(pendingLeaves.data);
    } catch (error) {
        showToast('Error loading leaves', 'error');
    }
}

function renderMyLeaves(leaves) {
    const tbody = document.getElementById('myLeavesTableBody');
    tbody.innerHTML = leaves.map(leave => `
        <tr>
            <td>${leave.leaveType}</td>
            <td>${leave.startDate}</td>
            <td>${leave.endDate}</td>
            <td>${leave.days}</td>
            <td>${leave.reason || '-'}</td>
            <td class="status-${leave.status.toLowerCase()}">${leave.status}</td>
        </tr>
    `).join('');
}

function renderPendingLeaves(leaves) {
    const tbody = document.getElementById('pendingLeavesTableBody');
    tbody.innerHTML = leaves.map(leave => `
        <tr>
            <td>${leave.employeeName}</td>
            <td>${leave.leaveType}</td>
            <td>${leave.startDate}</td>
            <td>${leave.endDate}</td>
            <td>${leave.days}</td>
            <td>${leave.reason || '-'}</td>
            <td class="actions">
                <button class="btn btn-sm btn-success" onclick="approveLeave(${leave.leaveId})">Approve</button>
                <button class="btn btn-sm btn-danger" onclick="rejectLeave(${leave.leaveId})">Reject</button>
            </td>
        </tr>
    `).join('');
}

document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
        document.getElementById(`${btn.dataset.tab}Tab`).classList.add('active');
    });
});

document.getElementById('applyLeaveBtn').addEventListener('click', () => {
    openModal('Apply Leave', `
        <form id="leaveForm">
            <div class="form-group">
                <label>Leave Type</label>
                <select id="leaveType" required>
                    <option value="CASUAL">Casual</option>
                    <option value="SICK">Sick</option>
                    <option value="PAID">Paid</option>
                    <option value="UNPAID">Unpaid</option>
                    <option value="MATERNITY">Maternity</option>
                    <option value="PATERNITY">Paternity</option>
                </select>
            </div>
            <div class="form-group">
                <label>Start Date</label>
                <input type="date" id="leaveStartDate" required>
            </div>
            <div class="form-group">
                <label>End Date</label>
                <input type="date" id="leaveEndDate" required>
            </div>
            <div class="form-group">
                <label>Reason</label>
                <textarea id="leaveReason" rows="3"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Submit</button>
        </form>
    `);
    document.getElementById('leaveForm').addEventListener('submit', saveLeave);
});

async function saveLeave(e) {
    e.preventDefault();
    try {
        await apiCall('/leaves', 'POST', {
            empId: currentUser.empId,
            leaveType: document.getElementById('leaveType').value,
            startDate: document.getElementById('leaveStartDate').value,
            endDate: document.getElementById('leaveEndDate').value,
            reason: document.getElementById('leaveReason').value
        });
        closeModal();
        loadLeaves();
        showToast('Leave applied successfully');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function approveLeave(leaveId) {
    try {
        await apiCall(`/leaves/${leaveId}/approve?approvedBy=${currentUser.userId}`, 'PUT');
        loadLeaves();
        showToast('Leave approved');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function rejectLeave(leaveId) {
    try {
        await apiCall(`/leaves/${leaveId}/reject?approvedBy=${currentUser.userId}`, 'PUT');
        loadLeaves();
        showToast('Leave rejected');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// ==================== Payroll ====================
async function loadPayroll() {
    const month = document.getElementById('payrollMonth').value;
    const year = document.getElementById('payrollYear').value;
    try {
        const result = await apiCall(`/payroll?month=${month}&year=${year}`);
        renderPayrollTable(result.data);
    } catch (error) {
        showToast('Error loading payroll', 'error');
    }
}

function renderPayrollTable(payrolls) {
    const tbody = document.getElementById('payrollTableBody');
    tbody.innerHTML = payrolls.map(pay => `
        <tr>
            <td>${pay.payId}</td>
            <td>${pay.employeeName}</td>
            <td>$${pay.basicSalary.toFixed(2)}</td>
            <td>$${pay.bonus.toFixed(2)}</td>
            <td>$${pay.deductions.toFixed(2)}</td>
            <td>$${pay.netSalary.toFixed(2)}</td>
            <td>${pay.paidDate || 'Not Paid'}</td>
            <td class="actions">
                ${!pay.paidDate ? 
                    `<button class="btn btn-sm btn-success" onclick="markAsPaid(${pay.payId})">Mark Paid</button>` : 
                    '<span class="status-approved">Paid</span>'}
            </td>
        </tr>
    `).join('');
}

document.getElementById('filterPayrollBtn').addEventListener('click', loadPayroll);

document.getElementById('generatePayrollBtn').addEventListener('click', async () => {
    openModal('Generate Payroll', `
        <form id="payrollForm">
            <div class="form-group">
                <label>Employee</label>
                <select id="payEmpId" required></select>
            </div>
            <div class="form-group">
                <label>Month</label>
                <select id="payMonth" required>
                    <option value="1">January</option>
                    <option value="2">February</option>
                    <option value="3">March</option>
                    <option value="4">April</option>
                    <option value="5">May</option>
                    <option value="6">June</option>
                    <option value="7" selected>July</option>
                    <option value="8">August</option>
                    <option value="9">September</option>
                    <option value="10">October</option>
                    <option value="11">November</option>
                    <option value="12">December</option>
                </select>
            </div>
            <div class="form-group">
                <label>Year</label>
                <input type="number" id="payYear" value="2026" required>
            </div>
            <div class="form-group">
                <label>Basic Salary</label>
                <input type="number" id="payBasic" step="0.01" required>
            </div>
            <div class="form-group">
                <label>Bonus</label>
                <input type="number" id="payBonus" step="0.01" value="0">
            </div>
            <div class="form-group">
                <label>Deductions</label>
                <input type="number" id="payDeductions" step="0.01" value="0">
            </div>
            <button type="submit" class="btn btn-primary">Generate</button>
        </form>
    `);
    try {
        const result = await apiCall('/employees');
        const select = document.getElementById('payEmpId');
        select.innerHTML = result.data.map(e => 
            `<option value="${e.empId}">${e.firstName} ${e.lastName}</option>`
        ).join('');
    } catch (error) {
        showToast('Error loading employees', 'error');
    }
    document.getElementById('payrollForm').addEventListener('submit', savePayroll);
});

async function savePayroll(e) {
    e.preventDefault();
    try {
        await apiCall('/payroll', 'POST', {
            empId: parseInt(document.getElementById('payEmpId').value),
            payMonth: parseInt(document.getElementById('payMonth').value),
            payYear: parseInt(document.getElementById('payYear').value),
            basicSalary: parseFloat(document.getElementById('payBasic').value),
            bonus: parseFloat(document.getElementById('payBonus').value) || 0,
            deductions: parseFloat(document.getElementById('payDeductions').value) || 0
        });
        closeModal();
        loadPayroll();
        showToast('Payroll generated successfully');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function markAsPaid(payId) {
    try {
        await apiCall(`/payroll/${payId}/mark-paid`, 'PUT');
        loadPayroll();
        showToast('Payroll marked as paid');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// ==================== Users ====================
async function loadUsers() {
    try {
        const result = await apiCall('/users');
        renderUserTable(result.data);
    } catch (error) {
        showToast('Error loading users', 'error');
    }
}

function renderUserTable(users) {
    const tbody = document.getElementById('userTableBody');
    tbody.innerHTML = users.map(user => `
        <tr>
            <td>${user.userId}</td>
            <td>${user.username}</td>
            <td>${user.role}</td>
            <td>${user.employeeName || '-'}</td>
            <td class="${user.isActive ? 'status-active' : 'status-inactive'}">
                ${user.isActive ? 'Active' : 'Inactive'}
            </td>
            <td class="actions">
                <button class="btn btn-sm btn-secondary" onclick="editUser(${user.userId})">Edit</button>
                <button class="btn btn-sm btn-danger" onclick="deleteUser(${user.userId})">Delete</button>
            </td>
        </tr>
    `).join('');
}

document.getElementById('addUserBtn').addEventListener('click', () => {
    openModal('Add User', `
        <form id="userForm">
            <div class="form-group">
                <label>Username</label>
                <input type="text" id="newUsername" required>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" id="newPassword" required>
            </div>
            <div class="form-group">
                <label>Role</label>
                <select id="newRole" required>
                    <option value="ADMIN">Admin</option>
                    <option value="EMPLOYEE">Employee</option>
                </select>
            </div>
            <div class="form-group">
                <label>Link to Employee (optional)</label>
                <select id="newEmpId"></select>
            </div>
            <button type="submit" class="btn btn-primary">Save</button>
        </form>
    `);
    loadEmployeeDropdownForUser();
    document.getElementById('userForm').addEventListener('submit', saveUser);
});

async function loadEmployeeDropdownForUser() {
    try {
        const result = await apiCall('/employees');
        const select = document.getElementById('newEmpId');
        select.innerHTML = '<option value="">No Employee</option>' +
            result.data.map(e => `<option value="${e.empId}">${e.firstName} ${e.lastName}</option>`).join('');
    } catch (error) {
        showToast('Error loading employees', 'error');
    }
}

async function saveUser(e) {
    e.preventDefault();
    try {
        await apiCall('/users', 'POST', {
            username: document.getElementById('newUsername').value,
            password: document.getElementById('newPassword').value,
            role: document.getElementById('newRole').value,
            empId: document.getElementById('newEmpId').value || null
        });
        closeModal();
        loadUsers();
        showToast('User created successfully');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function deleteUser(userId) {
    if (!confirm('Are you sure you want to delete this user?')) return;
    try {
        await apiCall(`/users/${userId}`, 'DELETE');
        loadUsers();
        showToast('User deleted');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function editUser(userId) {
    try {
        const result = await apiCall(`/users/${userId}`);
        const user = result.data;
        openModal('Edit User', `
            <form id="editUserForm">
                <div class="form-group">
                    <label>Username</label>
                    <input type="text" id="editUsername" value="${user.username}" required>
                </div>
                <div class="form-group">
                    <label>New Password (leave blank to keep current)</label>
                    <input type="password" id="editPassword">
                </div>
                <div class="form-group">
                    <label>Role</label>
                    <select id="editRole" required>
                        <option value="ADMIN" ${user.role === 'ADMIN' ? 'selected' : ''}>Admin</option>
                        <option value="EMPLOYEE" ${user.role === 'EMPLOYEE' ? 'selected' : ''}>Employee</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Link to Employee (optional)</label>
                    <select id="editEmpId"></select>
                </div>
                <button type="submit" class="btn btn-primary">Update</button>
            </form>
        `);
        const empRes = await apiCall('/employees');
        const select = document.getElementById('editEmpId');
        select.innerHTML = '<option value="">No Employee</option>' +
            empRes.data.map(e => `<option value="${e.empId}" ${e.empId === user.empId ? 'selected' : ''}>${e.firstName} ${e.lastName}</option>`).join('');
        document.getElementById('editUserForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const data = {
                username: document.getElementById('editUsername').value,
                role: document.getElementById('editRole').value,
                empId: document.getElementById('editEmpId').value || null
            };
            const pw = document.getElementById('editPassword').value;
            if (pw) data.password = pw;
            try {
                await apiCall(`/users/${userId}`, 'PUT', data);
                closeModal();
                loadUsers();
                showToast('User updated');
            } catch (error) {
                showToast(error.message, 'error');
            }
        });
    } catch (error) {
        showToast('Error loading user', 'error');
    }
}

// ==================== Print & Extract ====================
function printPage() {
    document.body.classList.add('printing');
    window.print();
    document.body.classList.remove('printing');
}

function extractTable(tableId) {
    const table = document.getElementById(tableId);
    if (!table) { showToast('No table to export', 'error'); return; }
    const rows = table.querySelectorAll('tr');
    const csv = [];
    rows.forEach(row => {
        const cols = row.querySelectorAll('td, th');
        const rowData = [];
        cols.forEach(col => {
            let text = col.textContent.trim().replace(/"/g, '""');
            rowData.push('"' + text + '"');
        });
        csv.push(rowData.join(','));
    });
    const blob = new Blob([csv.join('\n')], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = tableId + '_' + new Date().toISOString().slice(0, 10) + '.csv';
    link.click();
    URL.revokeObjectURL(link.href);
    showToast('CSV exported successfully');
}

// ==================== Menu Toggle ====================
document.getElementById('menuToggle').addEventListener('click', () => {
    document.getElementById('sidebar').classList.toggle('open');
});

document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', () => {
        if (window.innerWidth <= 768) {
            document.getElementById('sidebar').classList.remove('open');
        }
    });
});

// ==================== Clock ====================
function updateClock() {
    const now = new Date();
    const el = document.getElementById('topbarClock');
    if (el) el.textContent = now.toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric', year: 'numeric' }) + '  ' + now.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
}
setInterval(updateClock, 10000);
updateClock();

// ==================== Modal Close ====================
document.querySelector('.modal-close').addEventListener('click', closeModal);
document.getElementById('modal').addEventListener('click', (e) => {
    if (e.target.id === 'modal') closeModal();
});

// ==================== Initialize ====================
document.addEventListener('DOMContentLoaded', () => {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('attStartDate').value = today;
    document.getElementById('attEndDate').value = today;
});
