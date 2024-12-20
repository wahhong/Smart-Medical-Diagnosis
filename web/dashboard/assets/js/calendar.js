const daysContainer = document.querySelector(".days"),
    nextBtn = document.querySelector(".next-btn"),
    prevBtn = document.querySelector(".prev-btn"),
    month = document.querySelector(".month");

const months = [
    "January", "February", "March", "April", "May", "June", "July",
    "August", "September", "October", "November", "December"
];

let selectedDate = null;

const date = new Date();
let currentMonth = date.getMonth();
let currentYear = date.getFullYear();

function renderCalendar() {
    date.setDate(1);
    const firstDay = new Date(currentYear, currentMonth, 1).getDay();
    const lastDay = new Date(currentYear, currentMonth + 1, 0).getDate();
    const prevLastDay = new Date(currentYear, currentMonth, 0).getDate();
    const lastDayIndex = new Date(currentYear, currentMonth + 1, 0).getDay();
    const nextDays = 7 - lastDayIndex - 1;

    month.innerHTML = `${months[currentMonth]} ${currentYear}`;

    let days = "";

    // Previous month days
    for (let x = firstDay; x > 0; x--) {
        days += `<div class="day prev">${prevLastDay - x + 1}</div>`;
    }

    // Current month days
    for (let i = 1; i <= lastDay; i++) {
        const dayDate = new Date(currentYear, currentMonth, i);
        if (
            i === new Date().getDate() &&
            currentMonth === new Date().getMonth() &&
            currentYear === new Date().getFullYear()
        ) {
            days += `<div class="day today" data-date="${currentYear}-${currentMonth + 1}-${i}">${i}</div>`;
        } else if (dayDate < new Date()) {
            // Disable past days
            days += `<div class="day disabled" data-date="${currentYear}-${currentMonth + 1}-${i}">${i}</div>`;
        } else {
            days += `<div class="day" data-date="${currentYear}-${currentMonth + 1}-${i}">${i}</div>`;
        }
    }

    // Next month days
    for (let j = 1; j <= nextDays; j++) {
        days += `<div class="day next">${j}</div>`;
    }

    daysContainer.innerHTML = days;

    // Add event listener for picking a date
    const dayElements = document.querySelectorAll(".day:not(.disabled):not(.prev):not(.next)");
    dayElements.forEach(day => {
        day.addEventListener("click", function () {
            selectDate(this);
        });
    });

    // Automatically select today's date when the calendar is rendered
    const todayElement = document.querySelector(".day.today");
    if (todayElement) {
        selectDate(todayElement); // Select today's date
    }
}

// Function to handle date selection
function selectDate(dayElement) {
    const pickedDate = dayElement.getAttribute("data-date");
    const today = new Date();
    const selectedDateObj = new Date(pickedDate);

    if (selectedDateObj < today.setHours(0, 0, 0, 0)) {
        console.log("Cannot select past date.");
        return;
    }

    if (selectedDate) {
        selectedDate.classList.remove("selected");
    }

    selectedDate = dayElement;
    selectedDate.classList.add("selected");

    document.getElementById("selectedDate").value = pickedDate;

    // Load available times for the selected date
    loadAvailableTimes();
}

// Function to load available times dynamically
function loadAvailableTimes() {
    const selectedDate = document.getElementById("selectedDate").value;
    const doctorID = document.getElementById("doctorID").value;

    fetch(`getAvailableTimes.jsp?selectedDate=${selectedDate}&id=${doctorID}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.text();
        })
        .then(html => {
            document.getElementById("timeSlotsContainer").innerHTML = html;
            addRadioListeners(); // Reattach radio button listeners after loading new times
        })
        .catch(error => console.error("Error loading times:", error));
}

// Function to update the selected time input
function updateSelectedTime() {
    const selectedRadio = document.querySelector('input[name="appt_time"]:checked');
    const selectedTimeInput = document.getElementById("selectedTime");

    if (selectedRadio) {
        selectedTimeInput.value = selectedRadio.value; // Update the input field with the selected time
    }
}

// Function to add event listeners for radio buttons
function addRadioListeners() {
    const radioButtons = document.querySelectorAll('input[name="appt_time"]');

    radioButtons.forEach(radio => {
        radio.addEventListener('change', function () {
            // Update the selected time input
            updateSelectedTime();

            // Remove the selected class from all labels
            document.querySelectorAll('.time-label').forEach(label => {
                label.classList.remove('selected');
            });

            // Add the selected class to the associated label
            const label = document.querySelector(`label[for="${this.id}"]`);
            if (label) {
                label.classList.add('selected');
            }
        });
    });
}

renderCalendar();

nextBtn.addEventListener("click", () => {
    currentMonth++;
    if (currentMonth > 11) {
        currentMonth = 0;
        currentYear++;
    }
    renderCalendar();
});

prevBtn.addEventListener("click", () => {
    currentMonth--;
    if (currentMonth < 0) {
        currentMonth = 11;
        currentYear--;
    }
    renderCalendar();
});