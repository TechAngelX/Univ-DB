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

            <div class="form-group">
                <label for="accType">Register as:</label>
                <select class="form-control" id="accType" name="accType" required>
                    <option value="">Select User Type</option>
                    <option value="1">Student</option>
                    <option value="2">Staff</option>
                </select>
                <p id="accType-error" class="text-danger"></p>
            </div>

            <div class="form-group" id="award-group" style="display: none;">
                <label for="award">Award:</label>
                <select class="form-control" id="award" name="awardname">
                    <option value="">Select Award</option>
                </select>
                <p id="award-error" class="text-danger"></p>
            </div>

            <div class="form-group" id="programme-group" style="display: none;">
                <label for="programme">Programme:</label>
                <select class="form-control" id="programme" name="programme">
                    <option value="">Select Programme</option>
                </select>
                <p id="programme-error" class="text-danger"></p>
            </div>

            <div class="form-group" id="staffrole-group" style="display: none;">
                <label for="staffrole">Staff Role:</label>
                <select class="form-control" id="staffrole" name="staffrole">
                    <option value="">Select Role</option>
                </select>
                <p id="staffrole-error" class="text-danger"></p>
            </div>

            <div class="form-group" id="dept-group" style="display: none;">
                <label for="dept">Department:</label>
                <select class="form-control" id="dept" name="dept">
                    <option value="">Select Department</option>
                </select>
                <p id="dept-error" class="text-danger"></p>
            </div>

            <!-- Hidden data-->

            <input type="hidden" id="progid" name="progid">
            <input type="hidden" id="studylevelid" name="studylevelid">
            <input type="hidden" id="degreetypeid" name="degreetypeid">
            <input type="hidden" id="deptid" name="deptid">
            <input type="hidden" id="staffroleid" name="staffroleid">

            <button type="submit" class="btn btn-primary btn-block">Register</button>
        </form>
    </div>
</div>

<script>
    function convertToUppercase() {
        var fname = document.getElementById('fname');
        var lname = document.getElementById('lname');
        fname.value = fname.value.toUpperCase();
        lname.value = lname.value.toUpperCase();
    }

    document.addEventListener("DOMContentLoaded", function() {
        const accTypeSelect = document.getElementById("accType");
        const awardGroup = document.getElementById("award-group");
        const programmeGroup = document.getElementById("programme-group");
        const staffRoleGroup = document.getElementById("staffrole-group");
        const deptGroup = document.getElementById("dept-group");
        const awardSelect = document.getElementById("award");
        const programmeSelect = document.getElementById("programme");
        const staffRoleSelect = document.getElementById("staffrole");
        const deptSelect = document.getElementById("dept");

        // Hidden input fields
        const progIdInput = document.getElementById("progid");
        const studyLevelIdInput = document.getElementById("studylevelid");
        const degreeTypeIdInput = document.getElementById("degreetypeid");
        const deptIdInput = document.getElementById("deptid");
        const staffRoleIdInput = document.getElementById("staffroleid");

        // Handle account type changes
        accTypeSelect.addEventListener("change", function() {
            if (accTypeSelect.value === "1") { // Student selected
                awardGroup.style.display = "block";
                programmeGroup.style.display = "block";
                staffRoleGroup.style.display = "none";
                deptGroup.style.display = "none";
            } else if (accTypeSelect.value === "2") { // Staff selected
                awardGroup.style.display = "none";
                programmeGroup.style.display = "none";
                staffRoleGroup.style.display = "block";
                deptGroup.style.display = "block";
            } else {
                awardGroup.style.display = "none";
                programmeGroup.style.display = "none";
                staffRoleGroup.style.display = "none";
                deptGroup.style.display = "none";
            }
        });

        // Fetch student choices
        fetch("StudentChoicesServlet")
            .then(response => response.json())
            .then(data => {
                const choices = {};
                data.forEach(choice => {
                    if (!choices[choice.AWARDNAME]) {
                        choices[choice.AWARDNAME] = [];
                    }
                    choices[choice.AWARDNAME].push(choice);
                });

                // Populate awards dropdown
                awardSelect.innerHTML = '<option value="">Select Award</option>';
                Object.keys(choices).forEach(award => {
                    const option = document.createElement('option');
                    option.value = award;
                    option.textContent = award;
                    awardSelect.appendChild(option);
                });

                // Update programmes based on selected award
                awardSelect.addEventListener("change", function() {
                    const selectedAward = this.value;
                    programmeSelect.innerHTML = '<option value="">Select Programme</option>';
                    if (choices[selectedAward]) {
                        choices[selectedAward].forEach(programme => {
                            const option = document.createElement('option');
                            option.value = programme.PROGID;
                            option.textContent = programme.PROGNAME;
                            programmeSelect.appendChild(option);
                        });
                    }
                });

                // Set hidden fields based on selected programme
                programmeSelect.addEventListener("change", function() {
                    const selectedProgId = this.value;
                    const selectedChoice = data.find(choice => choice.PROGID === selectedProgId);
                    if (selectedChoice) {
                        progIdInput.value = selectedChoice.PROGID;
                        studyLevelIdInput.value = selectedChoice.STUDYLEVELID;
                        degreeTypeIdInput.value = selectedChoice.DEGREETYPEID;
                        deptIdInput.value = selectedChoice.DEPTID;
                    } else {
                        progIdInput.value = '';
                        studyLevelIdInput.value = '';
                        degreeTypeIdInput.value = '';
                        deptIdInput.value = '';
                    }
                });
            })
            .catch(error => console.error('Error fetching student choices:', error));

        // Fetch staff roles
        fetch("StaffChoicesServlet")
            .then(response => response.json())
            .then(data => {
                staffRoleSelect.innerHTML = '<option value="">Select Role</option>';
                data.forEach(role => {
                    const option = document.createElement('option');
                    option.value = role.STAFFROLEID;
                    option.textContent = role.STAFFROLENAME;
                    staffRoleSelect.appendChild(option);
                });

                // Update hidden field based on selected staff role
                staffRoleSelect.addEventListener("change", function() {
                    const selectedRoleId = this.value;
                    staffRoleIdInput.value = selectedRoleId;
                });
            })
            .catch(error => console.error('Error fetching staff roles:', error));

        // Fetch departments
        fetch("DeptChoicesServlet")
            .then(response => response.json())
            .then(data => {
                deptSelect.innerHTML = '<option value="">Select Department</option>';
                data.forEach(department => {
                    const option = document.createElement('option');
                    option.value = department.DEPTID;
                    option.textContent = department.DEPTNAME;
                    deptSelect.appendChild(option);
                });

                // Update hidden field based on selected department
                deptSelect.addEventListener("change", function() {
                    const selectedDeptId = this.value;
                    deptIdInput.value = selectedDeptId;
                });
            })
            .catch(error => console.error('Error fetching departments:', error));

        // Call convertToUppercase on form submission
        const form = document.getElementById("registrationForm");
        form.addEventListener("submit", function(event) {
            convertToUppercase();
        });
    });
</script>
</body>
</html>
