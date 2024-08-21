<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration | Univ-DB</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body, html {
            height: 100%;
            margin: 0;
        }
        .form-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .form-box {
            width: 100%;
            max-width: 500px;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .text-danger {
            font-size: 0.875rem;
        }
    </style>
</head>
<body>
<div class="container form-container">
    <div class="form-box">
        <h1 class="text-center">Univ-DB</h1>
        <form id="registrationForm" action="RegisterServlet" method="POST">
            <h3 class="text-center">Registration</h3>
            <p class="text-center">Please complete the form to create an account</p>

            <!-- First Name and Last Name fields -->
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="fname"><strong>First Name:</strong></label>
                    <input class="form-control" type="text" name="fname" id="fname" placeholder="Enter your first name">
                    <p id="fname-error" class="text-danger"></p>
                </div>
                <div class="form-group col-md-6">
                    <label for="lname"><strong>Last Name:</strong></label>
                    <input class="form-control" type="text" name="lname" id="lname" placeholder="Enter your last name">
                    <p id="lname-error" class="text-danger"></p>
                </div>
            </div>

            <!-- Password and Confirm Password fields -->
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="pword"><strong>Password:</strong></label>
                    <input class="form-control" type="password" name="pword" id="pword" placeholder="Enter your password">
                    <p id="pword-error" class="text-danger"></p>
                </div>
                <div class="form-group col-md-6">
                    <label for="pwordConfirm"><strong>Confirm Password:</strong></label>
                    <input class="form-control" type="password" name="pwordConfirm" id="pwordConfirm" placeholder="Confirm your password">
                    <p id="pwordConfirm-error" class="text-danger"></p>
                </div>
            </div>

            <!-- Student or Staff Choice -->
            <div class="form-group">
                <label for="accType">Register as:</label>
                <select class="form-control" id="accType" name="accType" required>
                    <option value="">Select User Type</option>
                    <option value="1">Student</option>
                    <option value="2">Staff</option>
                </select>
                <p id="accType-error" class="text-danger"></p>
            </div>

            <!-- Degree Type for Students -->
            <div class="form-group" id="degreeType-group" style="display: none;">
                <label for="degreeType">Degree Type:</label>
                <select class="form-control" id="degreeType" name="degreeType">
                    <option value="">Select Degree Type</option>
                </select>
                <p id="degreeType-error" class="text-danger"></p>
            </div>

            <!-- Department and Programme for Students -->
            <div class="form-group" id="department-group" style="display: none;">
                <label for="department">Department:</label>
                <select class="form-control" id="department" name="department">
                    <option value="">Select Department</option>
                </select>
                <p id="department-error" class="text-danger"></p>
            </div>

            <div class="form-group" id="programme-group" style="display: none;">
                <label for="programme">Programme:</label>
                <select class="form-control" id="programme" name="programme">
                    <option value="">Select Programme</option>
                </select>
                <p id="programme-error" class="text-danger"></p>
            </div>

            <!-- Staff Role for Staff -->
            <div class="form-group" id="staffrole-group" style="display: none;">
                <label for="staffrole">Staff Role:</label>
                <select class="form-control" id="staffrole" name="staffrole">
                    <option value="">Select Role</option>
                    <option value="AS001">Academic Staff</option>
                    <option value="PS001">Professional Services</option>
                    <option value="SS001">Student Support Services</option>
                </select>
                <p id="staffrole-error" class="text-danger"></p>
            </div>

            <button type="submit" class="btn btn-primary btn-block">Register</button>
        </form>
    </div>
</div>

<!-- JavaScript Logic -->
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const accTypeSelect = document.getElementById("accType");
        const degreeTypeGroup = document.getElementById("degreeType-group");
        const departmentGroup = document.getElementById("department-group");
        const programmeGroup = document.getElementById("programme-group");
        const staffRoleGroup = document.getElementById("staffrole-group");

        // Handle account type changes
        accTypeSelect.addEventListener("change", function() {
            if (accTypeSelect.value === "1") { // Student selected
                degreeTypeGroup.style.display = "block";
                departmentGroup.style.display = "block";
                programmeGroup.style.display = "block";
                staffRoleGroup.style.display = "none";
            } else if (accTypeSelect.value === "2") { // Staff selected
                degreeTypeGroup.style.display = "none";
                departmentGroup.style.display = "none";
                programmeGroup.style.display = "none";
                staffRoleGroup.style.display = "block";
            } else {
                degreeTypeGroup.style.display = "none";
                departmentGroup.style.display = "none";
                programmeGroup.style.display = "none";
                staffRoleGroup.style.display = "none";
            }
        });

        // Fetch student choices from the servlet
        fetch("json/studentChoices.json")
            .then(response => response.json())
            .then(data => {
                // Populate degree types
                const degreeTypeSelect = document.getElementById("degreeType");
                degreeTypeSelect.innerHTML = '<option value="">Select Degree Type</option>'; // Clear existing options
                data.degreeTypes.forEach(degree => {
                    const option = document.createElement('option');
                    option.value = degree.value;
                    option.textContent = degree.text;
                    degreeTypeSelect.appendChild(option);
                });

                // Populate departments
                const departmentSelect = document.getElementById("department");
                departmentSelect.innerHTML = '<option value="">Select Department</option>'; // Clear existing options
                data.departments.forEach(department => {
                    const option = document.createElement('option');
                    option.value = department.value;
                    option.textContent = department.text;
                    departmentSelect.appendChild(option);
                });

                // Update programmes dynamically when a department is selected
                departmentSelect.addEventListener("change", function() {
                    const selectedDept = this.value;
                    const programmeSelect = document.getElementById("programme");
                    programmeSelect.innerHTML = '<option value="">Select Programme</option>'; // Clear existing options

                    if (data.programmes[selectedDept]) {
                        data.programmes[selectedDept].forEach(programme => {
                            const option = document.createElement('option');
                            option.value = programme;
                            option.textContent = programme;
                            programmeSelect.appendChild(option);

                        });
                    }
                });
            })
            .catch(error => console.error('Error fetching student choices:', error));
    });
</script>

</body>
</html>
