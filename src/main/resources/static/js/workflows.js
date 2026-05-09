function showModal(message) {
    document.getElementById("resultText").innerText = message;
    document.getElementById("resultModal").style.display = "block";
}

function closeModal() {
    document.getElementById("resultModal").style.display = "none";
}

function runWorkflow(workflowId, buttonElement) {

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
        })
        .catch(error => {
            console.error(error);
            showModal("Error running workflow");
        });
}