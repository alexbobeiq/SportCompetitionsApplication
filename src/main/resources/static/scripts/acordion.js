function toggleDropdown(index) {
    // Get the specific body element for the clicked header
    const body = document.getElementById(`body-${index}`);

    // Toggle the "active" class
    body.classList.toggle('active');
}