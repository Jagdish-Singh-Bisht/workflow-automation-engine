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

    const input = document.getElementById("workflowInput").value;

    if (!input || input.trim() === "") {
        showModal("Please enter input before running workflow.");
        return;
    }

    // 🧠 get current row
    const row = buttonElement.closest("tr");

    const emailEnabled = row.querySelector(".email-toggle").checked;
    const whatsappEnabled = row.querySelector(".wa-toggle").checked;

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