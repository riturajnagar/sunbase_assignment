let customersData=[];
let sortOrder = 'asc';

document.addEventListener('DOMContentLoaded', async function() {
    const token = localStorage.getItem('token');
    
    if (!token) {
        alert('No token found. Please login first.');
        return;
    }

    console.log('Token:', token);
    
    try {
        const response = await fetch('http://localhost:8088/customers/', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            console.log("Customer data:", data);
            customersData = data;
            const table = document.getElementById('customerTable');
            
            if (!table) {
                console.error('Table element with ID "customerTable" not found.');
                return;
            }

            const tbody = table.getElementsByTagName('tbody')[0];
            
            if (!tbody) {
                console.error('Table "tbody" element not found.');
                return;
            }

            // Clear existing rows (optional, if you want to reset the table first)
            tbody.innerHTML = '';

            if (Array.isArray(data)) {
                console.log("hi it is array")
                data.forEach(customer => {
                    console.log('Processing customer:', customer); // Check each customer object
                    const row = tbody.insertRow();
                    row.insertCell(0).innerText = customer.uuid;
                    row.insertCell(1).innerText = customer.first_name;
                    row.insertCell(2).innerText = customer.last_name;
                    row.insertCell(3).innerText = customer.street;
                    row.insertCell(4).innerText = customer.address;
                    row.insertCell(5).innerText = customer.city;
                    row.insertCell(6).innerText = customer.state;
                    row.insertCell(7).innerText = customer.email;
                    row.insertCell(8).innerText = customer.phone;
                    
                    const actionsCell = row.insertCell(9);
                    
                    const editButton = document.createElement('button');
                    editButton.innerText = 'Edit';
                    editButton.onclick = () => editCustomer(customer.uuid);
                    actionsCell.appendChild(editButton);
                    
                    const deleteButton = document.createElement('button');
                    deleteButton.innerText = 'Delete';
                    deleteButton.onclick = () => deleteCustomer(customer.uuid);
                    actionsCell.appendChild(deleteButton);
                });
            } else {
                console.error('Error: data.content is not an array or is undefined', data);
            }

          

            console.log('Table updated successfully');
        } else {
            alert('Failed to load customers. Status: ' + response.status);
        }
    } catch (error) {
        console.error('Error fetching customer data:', error);
        alert('Error fetching customer data');
    }
});

async function syncCustomers() {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch('http://localhost:8088/customers/sync', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.ok) {
            alert('Customers synced successfully!');
            fetchCustomers();
        }else if(response.status === 400){
            const data = await response.json();
            alert('Failed to sync customers. Error: ' + data.message);

        } else {
            alert('Failed to sync customers!');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

async function fetchCustomers() {
    const token = localStorage.getItem('token');
   

    try {
        const response = await fetch('http://localhost:8088/customers/', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        if (response.ok) {
            const customers = await response.json();
            const customerTable = document.getElementById('customerTable').getElementsByTagName('tbody')[0];
            customerTable.innerHTML = ''; // Clear existing table content

            // Populate the table with fetched customers
            customersData = customers;

            customers.forEach(customer => {
                const row = customerTable.insertRow();
                row.insertCell(0).innerText = customer.first_name;
                row.insertCell(1).innerText = customer.last_name;
                row.insertCell(2).innerText = customer.street;
                row.insertCell(3).innerText = customer.address;
                row.insertCell(4).innerText = customer.city;
                row.insertCell(5).innerText = customer.state;
                row.insertCell(6).innerText = customer.email;
                row.insertCell(7).innerText = customer.phone;
                const actionsCell = row.insertCell(9);
                const editButton = document.createElement('button');
                editButton.innerText = 'Edit';
                editButton.onclick = () => editCustomer(customer.uuid);
                actionsCell.appendChild(editButton);
                const deleteButton = document.createElement('button');
                deleteButton.innerText = 'Delete';
                deleteButton.onclick = () => deleteCustomer(customer.uuid);
                actionsCell.appendChild(deleteButton);
            });
        } else {
            console.error('Failed to fetch customers');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

function editCustomer(id) {
    // Redirect to edit form
    window.location.href = `edit_customer.html?id=${id}`;
}

async function deleteCustomer(id) {
    const token = localStorage.getItem('token');
    const response = await fetch(`http://localhost:8088/customers/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });

    if (response.ok) {
        location.reload();
    } else {
        alert('Failed to delete customer');
    }
}


function filterCustomers() {
    const searchQuery = document.getElementById('searchBox').value.toLowerCase();
    const filteredCustomers = customersData.filter(customer => {
        return customer.first_name.toLowerCase().includes(searchQuery) || 
               customer.last_name.toLowerCase().includes(searchQuery);
    });
    console.log("Filtered customers:", filteredCustomers);
    searchResults(filteredCustomers);
}

function searchResults(filteredCustomers) {
    const customerTable = document.getElementById('customerTable').getElementsByTagName('tbody')[0];
    customerTable.innerHTML = '';

    filteredCustomers.forEach(customer => {
    console.log("customer data after search: ", customer);
        const row = customerTable.insertRow();
        row.insertCell(0).innerText = customer.uuid;
        row.insertCell(1).innerText = customer.first_name;
        row.insertCell(2).innerText = customer.last_name;
        row.insertCell(3).innerText = customer.street;
        row.insertCell(4).innerText = customer.address;
        row.insertCell(5).innerText = customer.city;
        row.insertCell(6).innerText = customer.state;
        row.insertCell(7).innerText = customer.email;
        row.insertCell(8).innerText = customer.phone;
        const actionsCell = row.insertCell(9);
        const editButton = document.createElement('button');
        editButton.innerText = 'Edit';
        editButton.onclick = () => editCustomer(customer.uuid);
        actionsCell.appendChild(editButton);
        const deleteButton = document.createElement('button');
        deleteButton.innerText = 'Delete';
        deleteButton.onclick = () => deleteCustomer(customer.uuid);
        actionsCell.appendChild(deleteButton);
    });
}

function sortByCity() {
    if (sortOrder === 'asc') {
        customersData.sort((a, b) => a.city.localeCompare(b.city)); // Ascending order
        sortOrder = 'desc';
    } else {
        customersData.sort((a, b) => b.city.localeCompare(a.city)); // Descending order
        sortOrder = 'asc';
    }
    searchResults(customersData); // Display sorted customers
}