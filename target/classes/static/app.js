const sectionSchemas = {
  GENERAL_POINTS: [
    { key: "category", label: "Category", type: "select", options: ["Department Meeting", "Parent-Teacher Meeting", "Announcement"], required: true },
    { key: "summary", label: "Summary", type: "textarea", required: true }
  ],
  FACULTY_JOINED_RELIEVED: [
    { key: "facultyName", label: "Faculty Name", type: "text", required: true },
    { key: "designation", label: "Designation", type: "text", required: true },
    { key: "action", label: "Action", type: "select", options: ["Joined", "Relieved"], required: true },
    { key: "actionDate", label: "Joining/Relieving Date", type: "date", required: true }
  ],
  FACULTY_ACHIEVEMENTS: [
    { key: "facultyName", label: "Faculty Name", type: "text", required: true },
    { key: "achievementType", label: "Achievement Type", type: "select", options: ["Award", "Guest Lecture", "Reviewer/Jury"], required: true },
    { key: "description", label: "Description", type: "textarea", required: true }
  ],
  STUDENT_ACHIEVEMENTS: [
    { key: "studentName", label: "Student Name", type: "text", required: true },
    { key: "rollNumber", label: "Roll Number", type: "text", required: true },
    { key: "department", label: "Department", type: "text", required: true },
    { key: "achievement", label: "Achievement", type: "textarea", required: true }
  ],
  DEPARTMENT_ACHIEVEMENTS: [
    { key: "milestone", label: "Milestone", type: "text", required: true },
    { key: "details", label: "Details", type: "textarea", required: true }
  ],
  FACULTY_EVENTS_CONDUCTED: [
    { key: "eventName", label: "Event Name", type: "text", required: true },
    { key: "eventType", label: "Event Type", type: "select", options: ["FDP", "Workshop", "STTP"], required: true },
    { key: "resourcePerson", label: "Resource Person", type: "text", required: true },
    { key: "participantCount", label: "Participation Count", type: "number", required: true }
  ],
  STUDENT_EVENTS_CONDUCTED: [
    { key: "eventName", label: "Event Name", type: "text", required: true },
    { key: "eventType", label: "Event Type", type: "select", options: ["Technical Workshop", "Guest Lecture"], required: true },
    { key: "topic", label: "Topic", type: "text", required: true },
    { key: "coordinator", label: "Coordinator", type: "text", required: true },
    { key: "participantCount", label: "Participant Count", type: "number", required: true }
  ],
  NON_TECHNICAL_EVENTS_CONDUCTED: [
    { key: "eventName", label: "Event Name", type: "text", required: true },
    { key: "category", label: "Category", type: "select", options: ["Cultural", "Social", "Institutional"], required: true },
    { key: "coordinator", label: "Coordinator", type: "text", required: true },
    { key: "participantCount", label: "Participant Count", type: "number", required: true }
  ],
  INDUSTRY_COLLEGE_VISITS: [
    { key: "institutionName", label: "Institution Name", type: "text", required: true },
    { key: "location", label: "Location", type: "text", required: true },
    { key: "coordinator", label: "Coordinator", type: "text", required: true },
    { key: "studentCount", label: "Student Count", type: "number", required: true },
    { key: "visitDates", label: "Visit Dates", type: "text", required: true }
  ],
  HACKATHON_EXTERNAL_PARTICIPATION: [
    { key: "eventName", label: "Event Name", type: "text", required: true },
    { key: "organizer", label: "Organizer", type: "text", required: true },
    { key: "mentor", label: "Mentor", type: "text", required: true },
    { key: "studentCount", label: "Student Count", type: "number", required: true }
  ],
  FACULTY_FDP_CERTIFICATION_ATTENDANCE: [
    { key: "facultyName", label: "Faculty Name", type: "text", required: true },
    { key: "platform", label: "Platform", type: "select", options: ["NPTEL", "Coursera", "EDX", "Industry Certification"], required: true },
    { key: "courseTitle", label: "Course/Certification", type: "text", required: true },
    { key: "status", label: "Status", type: "select", options: ["Ongoing", "Completed"], required: true }
  ],
  FACULTY_VISITS: [
    { key: "facultyName", label: "Faculty Name", type: "text", required: true },
    { key: "destination", label: "Visited Institution/Industry", type: "text", required: true },
    { key: "purpose", label: "Purpose", type: "textarea", required: true },
    { key: "visitDate", label: "Visit Date", type: "date", required: true }
  ],
  PATENTS_PUBLISHED: [
    { key: "title", label: "Title", type: "text", required: true },
    { key: "applicationNumber", label: "Application Number", type: "text", required: true },
    { key: "publicationDate", label: "Publication Date", type: "date", required: true }
  ],
  VEDIC_PROGRAMS: [
    { key: "programName", label: "Program Name", type: "text", required: true },
    { key: "center", label: "Center", type: "select", options: ["Hyderabad", "Bangalore"], required: true },
    { key: "audience", label: "Audience", type: "select", options: ["Students", "Faculty", "Both"], required: true },
    { key: "participantCount", label: "Participant Count", type: "number", required: true }
  ],
  PLACEMENTS: [
    { key: "companyName", label: "Company Name", type: "text", required: true },
    { key: "department", label: "Department", type: "text", required: true },
    { key: "studentsPlaced", label: "Students Placed", type: "number", required: true },
    { key: "packageDetails", label: "Package Details", type: "text", required: true }
  ],
  MOUS_SIGNED: [
    { key: "organizationName", label: "Organization Name", type: "text", required: true },
    { key: "signingDate", label: "Signing Date", type: "date", required: true },
    { key: "validityPeriod", label: "Validity Period", type: "text", required: true },
    { key: "purpose", label: "Purpose", type: "textarea", required: true }
  ],
  SKILL_DEVELOPMENT_PROGRAMS: [
    { key: "topic", label: "Training Topic", type: "text", required: true },
    { key: "coordinator", label: "Coordinator", type: "text", required: true },
    { key: "sessions", label: "Sessions", type: "number", required: true },
    { key: "studentCount", label: "Student Count", type: "number", required: true }
  ]
};

const sectionTypes = Object.keys(sectionSchemas);

const sectionSelect = document.getElementById("sectionType");
const addEntryBtn = document.getElementById("addEntryBtn");
sectionTypes.forEach(type => {
  const option = document.createElement("option");
  option.value = type;
  option.textContent = type;
  sectionSelect.appendChild(option);
});

const sectionFields = document.getElementById("sectionFields");
const contentPreview = document.getElementById("contentPreview");

function buildField(field) {
  let input;
  if (field.type === "select") {
    input = document.createElement("select");
    field.options.forEach(optionValue => {
      const option = document.createElement("option");
      option.value = optionValue;
      option.textContent = optionValue;
      input.appendChild(option);
    });
  } else if (field.type === "textarea") {
    input = document.createElement("textarea");
    input.rows = 3;
  } else {
    input = document.createElement("input");
    input.type = field.type;
  }
  input.dataset.key = field.key;
  input.dataset.required = field.required ? "true" : "false";
  input.placeholder = field.label;
  return input;
}

function renderSectionFields() {
  const schema = sectionSchemas[sectionSelect.value] || [];
  sectionFields.innerHTML = "";
  schema.forEach(field => {
    const input = buildField(field);
    sectionFields.appendChild(input);
  });
  updateContentPreview();
}

function collectSectionData() {
  const data = {};
  const inputs = sectionFields.querySelectorAll("input, select, textarea");
  inputs.forEach(input => {
    const key = input.dataset.key;
    const value = input.value?.trim();
    if (value) {
      if (input.type === "number") {
        data[key] = Number(value);
      } else {
        data[key] = value;
      }
    } else {
      data[key] = "";
    }
  });
  return data;
}

function validateSectionData() {
  const inputs = sectionFields.querySelectorAll("input, select, textarea");
  for (const input of inputs) {
    const required = input.dataset.required === "true";
    if (required && !input.value.trim()) {
      throw new Error(`Please fill: ${input.placeholder}`);
    }
  }
}

function updateContentPreview() {
  const data = collectSectionData();
  contentPreview.textContent = JSON.stringify(data, null, 2);
}

sectionFields.addEventListener("input", updateContentPreview);
sectionSelect.addEventListener("change", renderSectionFields);
renderSectionFields();

let token = "";
let editingEntryId = null;
let currentUserRole = "";

const navUser = document.getElementById("navUser");
const logoutBtn = document.getElementById("logoutBtn");

function setStatus(id, message, isError = false) {
  const el = document.getElementById(id);
  if (!el) return;
  el.textContent = message;
  el.style.color = isError ? "#b91c1c" : "#065f46";
}

function requireToken() {
  if (!token) {
    throw new Error("Please login first.");
  }
}

function setGatedVisibility(isVisible) {
  document.querySelectorAll(".gated").forEach(section => {
    section.classList.toggle("visible", isVisible);
  });
}

function setCreateAccess() {
  const createSection = document.getElementById("create");
  if (!createSection) return;
  const allowed = currentUserRole === "COORDINATOR" || currentUserRole === "ADMIN";
  createSection.querySelector("#createWeekBtn").disabled = !allowed;
  createSection.classList.toggle("disabled", !allowed);
  if (!allowed) {
    setStatus("createWeekStatus", "Only COORDINATOR or ADMIN can create weekly reports.", true);
  } else {
    setStatus("createWeekStatus", "");
  }
}

function setRoleAccess() {
  setCreateAccess();
  const entrySection = document.getElementById("entry");
  const dashboardSection = document.getElementById("dashboard");
  const entriesSection = document.getElementById("entries");
  const previewSection = document.getElementById("preview");

  const canContribute = ["FACULTY", "COORDINATOR", "ADMIN"].includes(currentUserRole);
  const canReview = ["COORDINATOR", "ADMIN"].includes(currentUserRole);

  if (entrySection) {
    entrySection.classList.toggle("disabled", !canContribute);
    entrySection.querySelector("#addEntryBtn").disabled = !canContribute;
    if (!canContribute) {
      setStatus("entryStatus", "You do not have permission to add entries.", true);
    }
  }
  if (dashboardSection) {
    dashboardSection.classList.toggle("disabled", !canReview);
  }
  if (entriesSection) {
    entriesSection.classList.toggle("disabled", !canReview);
  }
  if (previewSection) {
    previewSection.classList.toggle("disabled", !canReview);
  }
}

function setNavUser() {
  if (!navUser) return;
  navUser.textContent = token ? `Role: ${currentUserRole}` : "Not logged in";
}

async function api(path, options = {}) {
  const headers = options.headers || {};
  headers["Content-Type"] = "application/json";
  if (token) headers["Authorization"] = `Bearer ${token}`;

  const response = await fetch(path, { ...options, headers });
  if (!response.ok) {
    const contentType = response.headers.get("content-type") || "";
    let errorText = "";
    if (contentType.includes("application/json")) {
      const body = await response.json().catch(() => ({}));
      errorText = body.error || JSON.stringify(body);
    } else {
      errorText = await response.text();
    }
    throw new Error(errorText || "Request failed");
  }
  return response.headers.get("content-type")?.includes("application/json")
    ? response.json()
    : null;
}

document.getElementById("loginBtn").addEventListener("click", async () => {
  try {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const result = await api("/api/auth/login", {
      method: "POST",
      body: JSON.stringify({ email, password })
    });
    token = result.token;
    currentUserRole = result.role;
    setNavUser();
    setStatus("loginStatus", `Logged in as ${result.fullName} (${result.role})`);
    setStatus("createWeekStatus", "");
    setStatus("entryStatus", "");
    setStatus("dashboardStatus", "");
    setStatus("entriesStatus", "");
    setStatus("listReportsStatus", "");
    setStatus("previewStatus", "");
    setGatedVisibility(true);
    setRoleAccess();
  } catch (e) {
    setStatus("loginStatus", "Login failed: " + e.message, true);
  }
});

document.getElementById("createWeekBtn").addEventListener("click", async () => {
  try {
    requireToken();
    if (currentUserRole !== "COORDINATOR" && currentUserRole !== "ADMIN") {
      throw new Error("Only COORDINATOR or ADMIN can create weekly reports.");
    }
    const weekStart = document.getElementById("weekStart").value;
    const weekEnd = document.getElementById("weekEnd").value;
    if (!weekStart || !weekEnd) {
      throw new Error("Please select both week start and week end.");
    }
    if (weekEnd < weekStart) {
      throw new Error("Week end cannot be before week start.");
    }
    const result = await api("/api/reports", {
      method: "POST",
      body: JSON.stringify({ weekStart, weekEnd })
    });
    setStatus("createWeekStatus", `Weekly report created with ID: ${result.id}`);
  } catch (e) {
    setStatus("createWeekStatus", e.message, true);
  }
});

document.getElementById("addEntryBtn").addEventListener("click", async () => {
  try {
    requireToken();
    const weeklyReportId = Number(document.getElementById("weeklyReportId").value);
    const sectionType = document.getElementById("sectionType").value;
    const eventDate = document.getElementById("eventDate").value;
    validateSectionData();
    const contentJson = JSON.stringify(collectSectionData());
    if (!weeklyReportId) {
      throw new Error("Please enter a weekly report ID.");
    }
    if (!eventDate) {
      throw new Error("Please select an event date.");
    }
    const endpoint = editingEntryId ? `/api/reports/entries/${editingEntryId}` : "/api/reports/entries";
    const result = await api(endpoint, {
      method: editingEntryId ? "PUT" : "POST",
      body: JSON.stringify({ weeklyReportId, sectionType, eventDate, contentJson })
    });
    if (editingEntryId) {
      setStatus("entryStatus", `Entry updated: ${result.id}`);
      editingEntryId = null;
      addEntryBtn.textContent = "Add Entry";
    } else {
      setStatus("entryStatus", `Entry created: ${result.id}`);
    }
    const entriesWeekId = Number(document.getElementById("entriesWeekId").value);
    if (entriesWeekId && entriesWeekId === weeklyReportId) {
      const entries = await api(`/api/reports/entries?weeklyReportId=${weeklyReportId}`);
      renderEntries(entries);
    }
  } catch (e) {
    setStatus("entryStatus", e.message, true);
  }
});

document.getElementById("loadDashboardBtn").addEventListener("click", async () => {
  try {
    requireToken();
    const weeklyReportId = Number(document.getElementById("dashboardWeekId").value);
    if (!weeklyReportId) {
      throw new Error("Please enter a weekly report ID.");
    }
    const result = await api(`/api/dashboard?weeklyReportId=${weeklyReportId}`);
    renderDashboardSummary(result);
    document.getElementById("dashboardOutput").textContent = JSON.stringify(result, null, 2);
    setStatus("dashboardStatus", "Dashboard loaded.");
  } catch (e) {
    setStatus("dashboardStatus", e.message, true);
  }
});

function renderDashboardSummary(dashboard) {
  const container = document.getElementById("dashboardSummary");
  if (!container) return;
  container.innerHTML = "";
  const entries = Object.entries(dashboard.sectionCompletion || {});
  entries.forEach(([section, status]) => {
    const card = document.createElement("div");
    const badge = status === "COMPLETE" ? "badge complete" : "badge pending";
    card.innerHTML = `<strong>${section.replaceAll("_", " ")}</strong><div class="${badge}">${status}</div>`;
    container.appendChild(card);
  });
}

document.getElementById("listReportsBtn").addEventListener("click", async () => {
  try {
    requireToken();
    const result = await api("/api/reports");
    document.getElementById("listReportsOutput").textContent = JSON.stringify(result, null, 2);
    setStatus("listReportsStatus", "Reports loaded.");
  } catch (e) {
    setStatus("listReportsStatus", e.message, true);
  }
});

document.getElementById("loadEntriesBtn").addEventListener("click", async () => {
  try {
    requireToken();
    const weeklyReportId = Number(document.getElementById("entriesWeekId").value);
    if (!weeklyReportId) {
      throw new Error("Please enter a weekly report ID.");
    }
    const entries = await api(`/api/reports/entries?weeklyReportId=${weeklyReportId}`);
    renderEntries(entries);
    setStatus("entriesStatus", "Entries loaded.");
  } catch (e) {
    setStatus("entriesStatus", e.message, true);
  }
});

document.getElementById("loadPreviewBtn").addEventListener("click", async () => {
  try {
    requireToken();
    const weeklyReportId = Number(document.getElementById("previewWeekId").value);
    if (!weeklyReportId) {
      throw new Error("Please enter a weekly report ID.");
    }
    const reports = await api("/api/reports");
    const report = reports.find(r => r.id === weeklyReportId);
    if (!report) {
      throw new Error("Weekly report not found.");
    }
    const entries = await api(`/api/reports/entries?weeklyReportId=${weeklyReportId}`);
    renderPreview(report, entries);
    setStatus("previewStatus", "Preview loaded.");
  } catch (e) {
    setStatus("previewStatus", e.message, true);
  }
});

function renderPreview(report, entries) {
  const container = document.getElementById("previewContainer");
  if (!container) return;
  const grouped = {};
  entries.forEach(entry => {
    grouped[entry.sectionType] = grouped[entry.sectionType] || [];
    grouped[entry.sectionType].push(entry);
  });

  const sectionsHtml = sectionTypes.map(section => {
    const sectionEntries = grouped[section] || [];
    const entryHtml = sectionEntries.length
      ? sectionEntries.map((entry, index) => {
          const content = formatContentForDisplay(entry.contentJson);
          return `
            <div class="preview-entry">
              <strong>${index + 1}. Event Date:</strong> ${entry.eventDate}<br/>
              <strong>Contributor:</strong> ${entry.contributedByName}<br/>
              <div>${content}</div>
            </div>
          `;
        }).join("")
      : `<div class="preview-entry">No entries recorded.</div>`;
    return `
      <div class="preview-section">
        <h3>${section.replaceAll("_", " ")}</h3>
        ${entryHtml}
      </div>
    `;
  }).join("");

  container.innerHTML = `
    <div class="preview-report">
      <h2>Weekly Report</h2>
      <p><strong>Report ID:</strong> ${report.id}</p>
      <p><strong>Week:</strong> ${report.weekStart} to ${report.weekEnd}</p>
      <p><strong>Status:</strong> ${report.status}</p>
      ${sectionsHtml}
    </div>
  `;
}

function formatContentForDisplay(contentJson) {
  if (!contentJson) return "";
  try {
    const parsed = JSON.parse(contentJson);
    if (Array.isArray(parsed)) {
      return `<ul>${parsed.map(item => `<li>${escapeHtml(String(item))}</li>`).join("")}</ul>`;
    }
    if (typeof parsed === "object" && parsed !== null) {
      const lines = Object.entries(parsed).map(([key, value]) => {
        const label = key.replace(/([A-Z])/g, " $1").replace(/^./, c => c.toUpperCase());
        return `<div><strong>${escapeHtml(label)}:</strong> ${escapeHtml(String(value))}</div>`;
      }).join("");
      return lines;
    }
  } catch (e) {
    return `<div>${escapeHtml(contentJson)}</div>`;
  }
  return `<div>${escapeHtml(contentJson)}</div>`;
}

function escapeHtml(text) {
  return text.replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;");
}

function renderEntries(entries) {
  const container = document.getElementById("entriesContainer");
  if (!container) return;
  if (!entries || entries.length === 0) {
    container.innerHTML = "<p>No entries found for this week.</p>";
    return;
  }
  const rows = entries.map(entry => {
    const content = entry.contentJson || "";
    const safeContent = content.replace(/</g, "&lt;").replace(/>/g, "&gt;");
    return `
      <tr>
        <td>${entry.id}</td>
        <td>${entry.sectionType}</td>
        <td>${entry.eventDate}</td>
        <td>${entry.contributedByName}</td>
        <td><pre>${safeContent}</pre></td>
        <td>
          <div class="actions">
            <button class="btn-secondary" data-action="edit" data-id="${entry.id}">Edit</button>
            <button class="btn-danger" data-action="delete" data-id="${entry.id}">Delete</button>
          </div>
        </td>
      </tr>
    `;
  }).join("");

  container.innerHTML = `
    <table class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Section</th>
          <th>Date</th>
          <th>Contributor</th>
          <th>Content</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>${rows}</tbody>
    </table>
  `;

  container.querySelectorAll("button[data-action]").forEach(button => {
    button.addEventListener("click", () => {
      const action = button.dataset.action;
      const id = Number(button.dataset.id);
      const entry = entries.find(e => e.id === id);
      if (!entry) return;
      if (action === "edit") {
        startEditEntry(entry);
      } else if (action === "delete") {
        deleteEntry(id);
      }
    });
  });
}

function startEditEntry(entry) {
  editingEntryId = entry.id;
  addEntryBtn.textContent = "Save Changes";
  document.getElementById("weeklyReportId").value = entry.weeklyReportId;
  document.getElementById("eventDate").value = entry.eventDate;
  sectionSelect.value = entry.sectionType;
  renderSectionFields();
  let parsed = {};
  try {
    parsed = JSON.parse(entry.contentJson || "{}");
  } catch (e) {
    parsed = {};
  }
  const inputs = sectionFields.querySelectorAll("input, select, textarea");
  inputs.forEach(input => {
    const key = input.dataset.key;
    if (parsed[key] !== undefined) {
      input.value = parsed[key];
    }
  });
  updateContentPreview();
  setStatus("entryStatus", `Editing entry #${entry.id}. Click Save Changes to update.`);
}

async function deleteEntry(entryId) {
  try {
    await api(`/api/reports/entries/${entryId}`, { method: "DELETE" });
    setStatus("entriesStatus", `Entry ${entryId} deleted.`);
    const weeklyReportId = Number(document.getElementById("entriesWeekId").value);
    if (weeklyReportId) {
      const entries = await api(`/api/reports/entries?weeklyReportId=${weeklyReportId}`);
      renderEntries(entries);
    }
  } catch (e) {
    setStatus("entriesStatus", e.message, true);
  }
}

setGatedVisibility(false);
setNavUser();

logoutBtn?.addEventListener("click", () => {
  token = "";
  currentUserRole = "";
  editingEntryId = null;
  addEntryBtn.textContent = "Add Entry";
  setGatedVisibility(false);
  setNavUser();
  setStatus("loginStatus", "Logged out.");
});
