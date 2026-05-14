// alert("NEW JS LOADED");

let isWorkflowRunning = false;

function showModal(message) {
    document.getElementById("resultText").innerText = message;
    document.getElementById("resultModal").style.display = "block";
}

function closeModal() {
    document.getElementById("resultModal").style.display = "none";
}

console.log("LOCK STATE: ", isWorkflowRunning);


function runWorkflow(workflowId, buttonElement) {

    if(isWorkflowRunning) {
        return;
    }

    isWorkflowRunning = true;

    console.log("Clicked Run for:", workflowId);

    let inputField = document.getElementById("workflowInput");

    let input;

    if(inputField) {
        input = inputField.value;

        if (!input || input.trim() === "") {
            showModal("Please enter input before running workflow.");
            return;
        }

    } else {

        input = prompt("Enter input for workflow:");

        if (!input || input.trim() === "") {
            return;
        }
    }

    // 🧠 get current row
    let emailEnabled = true;
    let whatsappEnabled = false;

    if(buttonElement) {

        const row = buttonElement.closest("tr");

        if(row) {
            emailEnabled = row.querySelector(".email-toggle").checked;
            whatsappEnabled = row.querySelector(".wa-toggle").checked;
        }
    }

    console.log("Email:", emailEnabled, "WhatsApp:", whatsappEnabled);

    // Loading state
    buttonElement.disabled = true;
    buttonElement.innerText = "Running...";

    fetch('/api/workflows/' + workflowId + '/run', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            input: input,
            emailEnabled: emailEnabled,
            whatsappEnabled: whatsappEnabled
        })
    })
        .then(response => response.text())
        .then(data => {
            showModal(data);

            buttonElement.disabled = false;
            buttonElement.innerText = "Run";
            isWorkflowRunning = false;
        })
        .catch(error => {
            console.error(error);
            showModal("Error running workflow");

            buttonElement.disabled = false;
            buttonElement.innerText = "Run";
            isWorkflowRunning = false;
        });
}