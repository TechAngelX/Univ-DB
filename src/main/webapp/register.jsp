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

            <!-- Award Dropdown for Students -->
            <div class="form-group" id="award-group" style="display: none;">
                <label for="award">Award:</label>
                <select class="form-control" id="award" name="award">
                    <option value="">Select Award</option>
                </select>
                <p id="award-error" class="text-danger"></p>
            </div>

            <!-- Programme Dropdown for Students -->
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
        const awardGroup = document.getElementById("award-group");
        const programmeGroup = document.getElementById("programme-group");
        const staffRoleGroup = document.getElementById("staffrole-group");
        const awardSelect = document.getElementById("award");
        const programmeSelect = document.getElementById("programme");

        // Handle account type changes
        accTypeSelect.addEventListener("change", function() {
            if (accTypeSelect.value === "1") { // Student selected
                awardGroup.style.display = "block";
                programmeGroup.style.display = "block";
                staffRoleGroup.style.display = "none";
            } else if (accTypeSelect.value === "2") { // Staff selected
                awardGroup.style.display = "none";
                programmeGroup.style.display = "none";
                staffRoleGroup.style.display = "block";
            } else {
                awardGroup.style.display = "none";
                programmeGroup.style.display = "none";
                staffRoleGroup.style.display = "none";
            }
        });

        // Fetch awards and programmes from the servlet
        fetch("StudentChoicesServlet")
            .then(response => response.json())
            .then(data => {
                const awards = {};

                // Group the data by award name
                data.forEach(choice => {
                    if (!awards[choice.AWARDNAME]) {
                        awards[choice.AWARDNAME] = [];
                    }
                    awards[choice.AWARDNAME].push(choice);
                });

                // Populate awards dropdown
                awardSelect.innerHTML = '<option value="">Select Award</option>';
                Object.keys(awards).forEach(award => {
                    const option = document.createElement('option');
                    option.value = award;
                    option.textContent = award;
                    awardSelect.appendChild(option);
                });

                // Update programmes dynamically when an award is selected
                awardSelect.addEventListener("change", function() {
                    const selectedAward = this.value;
                    programmeSelect.innerHTML = '<option value="">Select Programme</option>';

                    if (awards[selectedAward]) {
                        awards[selectedAward].forEach(programme => {
                            const option = document.createElement('option');
                            option.value = programme.PROGID;
                            option.textContent = programme.PROGNAME;
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
