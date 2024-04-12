function setRole(username, role) {
    var adminCheckbox = document.getElementById('admin-checkbox-' + username);
    var ctvCheckbox = document.getElementById('ctv-checkbox-' + username);
    var userCheckbox = document.getElementById('user-checkbox-' + username);
    var selectedCheckbox = null;

    if (role === 'Admin') {
        selectedCheckbox = adminCheckbox;
    } else if (role === 'CTV') {
        selectedCheckbox = ctvCheckbox;
    } else if (role === 'User') {
        selectedCheckbox = userCheckbox;
    }

    if (selectedCheckbox.checked) {
        // Send an AJAX request to the server to add the selected role to the account
        var xhr = new XMLHttpRequest();
        xhr.open('POST', '/admin/phan-quyen/updateRole', true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    console.log('Role set successfully');
                } else {
                    console.error('Failed to set role');
                }
            }
        };
        xhr.send(JSON.stringify({
            username: username,
            role: role
        }));
    } else {
        // Send an AJAX request to the server to remove the selected role from the account
        var xhr = new XMLHttpRequest();
        xhr.open('POST', '/remove-role', true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    console.log('Role removed successfully');
                } else {
                    console.error('Failed to remove role');
                }
            }
        };
        xhr.send(JSON.stringify({
            username: username,
            role: role
        }));
    }
}






