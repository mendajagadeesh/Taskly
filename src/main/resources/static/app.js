const authStatus = document.getElementById("authStatus");
const taskList = document.getElementById("taskList");
const taskCount = document.getElementById("taskCount");
const taskControlsCard = document.getElementById("taskControlsCard");
const tasksPanel = document.getElementById("tasksPanel");
const tasksHeading = document.getElementById("tasksHeading");
const registerCard = document.getElementById("registerCard");
const loginCard = document.getElementById("loginCard");

const registerForm = document.getElementById("registerForm");
const loginForm = document.getElementById("loginForm");
const addTaskForm = document.getElementById("addTaskForm");
const findTaskForm = document.getElementById("findTaskForm");
const deleteTaskForm = document.getElementById("deleteTaskForm");
const logoutBtn = document.getElementById("logoutBtn");
const loadTasksBtn = document.getElementById("loadTasksBtn");

const tokenKey = "taskly_token";

function getToken() {
  return localStorage.getItem(tokenKey);
}

function setToken(token) {
  localStorage.setItem(tokenKey, token);
  updateAuthStatus();
}

function clearToken() {
  localStorage.removeItem(tokenKey);
  updateAuthStatus();
}

function updateAuthStatus() {
  const isLoggedIn = Boolean(getToken());
  authStatus.textContent = isLoggedIn ? "Logged in" : "Not logged in";
  authStatus.classList.toggle("online", isLoggedIn);
  logoutBtn.style.display = isLoggedIn ? "" : "none";
  registerCard.style.display = isLoggedIn ? "none" : "";
  loginCard.style.display = isLoggedIn ? "none" : "";
  taskControlsCard.style.display = isLoggedIn ? "" : "none";
  tasksPanel.style.display = isLoggedIn ? "" : "none";
}

function log(message, data) {
  const timestamp = new Date().toLocaleTimeString();
  if (data !== undefined) {
    console.log(`[${timestamp}] ${message}`, data);
    return;
  }
  console.log(`[${timestamp}] ${message}`);
}

function getUserId() {
  const value = document.getElementById("userId").value;
  const id = Number(value);
  if (!Number.isInteger(id) || id <= 0) {
    throw new Error("Please enter a valid positive User ID.");
  }
  return id;
}

function renderTasks(tasks) {
  taskList.innerHTML = "";
  tasks.forEach((task, index) => {
    const item = document.createElement("li");
    item.className = "task-item";
    item.style.animationDelay = `${80 + index * 70}ms`;

    const meta = document.createElement("div");
    meta.className = "task-meta";
    meta.innerHTML = `<strong>${task.taskName || "Untitled task"}</strong><span class="task-id">ID: ${task.id}</span>`;

    item.appendChild(meta);
    taskList.appendChild(item);
  });
  taskCount.textContent = `${tasks.length} item${tasks.length === 1 ? "" : "s"}`;
}

async function api(path, options = {}) {
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {})
  };

  const token = getToken();
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(path, { ...options, headers });
  const text = await response.text();
  let body = null;
  if (text) {
    try {
      body = JSON.parse(text);
    } catch {
      body = text;
    }
  }

  if (!response.ok) {
    const errorMessage =
      typeof body === "object" && body !== null
        ? body.message
        : String(body || `Request failed (${response.status})`);
    throw new Error(errorMessage);
  }

  return body;
}

registerForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  try {
    const payload = {
      name: document.getElementById("regName").value.trim(),
      email: document.getElementById("regEmail").value.trim(),
      password: document.getElementById("regPassword").value
    };
    const user = await api("/api/auth/register", {
      method: "POST",
      body: JSON.stringify(payload)
    });
    log("Registration successful", user);
    registerForm.reset();
  } catch (error) {
    log(error.message);
  }
});

loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  try {
    const payload = {
      email: document.getElementById("loginEmail").value.trim(),
      password: document.getElementById("loginPassword").value
    };
    const auth = await api("/api/auth/login", {
      method: "POST",
      body: JSON.stringify(payload)
    });
    setToken(auth.token);
    log("Login successful", { tokenType: auth.tokenType });
    loginForm.reset();
  } catch (error) {
    log(error.message);
  }
});

loadTasksBtn.addEventListener("click", async () => {
  try {
    const userId = getUserId();
    const tasks = await api(`/api/${userId}/tasks`);
    tasksHeading.textContent = "Tasks";
    renderTasks(tasks);
    log("Loaded all tasks", tasks);
  } catch (error) {
    log(error.message);
  }
});

addTaskForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  try {
    const userId = getUserId();
    const payload = {
      taskName: document.getElementById("taskName").value.trim()
    };
    const created = await api(`/api/${userId}/tasks`, {
      method: "POST",
      body: JSON.stringify(payload)
    });
    log("Task created", created);
    addTaskForm.reset();
    const tasks = await api(`/api/${userId}/tasks`);
    renderTasks(tasks);
  } catch (error) {
    log(error.message);
  }
});

findTaskForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  try {
    const userId = getUserId();
    const taskId = Number(document.getElementById("findTaskId").value);
    const task = await api(`/api/${userId}/tasks/${taskId}`);
    tasksHeading.textContent = `Search result — Task #${taskId}`;
    renderTasks([task]);
    findTaskForm.reset();
  } catch (error) {
    tasksHeading.textContent = "Tasks";
    taskList.innerHTML = `<li class="task-item" style="color:#ff8fa3;border-color:rgba(255,85,119,0.4)">${error.message}</li>`;
    taskCount.textContent = "0 items";
  }
});

deleteTaskForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  try {
    const userId = getUserId();
    const taskId = Number(document.getElementById("deleteTaskId").value);
    const message = await api(`/api/${userId}/tasks/${taskId}`, { method: "DELETE" });
    log("Task deleted", message);
    const tasks = await api(`/api/${userId}/tasks`);
    renderTasks(tasks);
  } catch (error) {
    log(error.message);
  }
});

logoutBtn.addEventListener("click", () => {
  clearToken();
  log("Logged out. Token removed from browser storage.");
});

function enableCardMotion() {
  if (window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
    return;
  }

  const cards = document.querySelectorAll(".card");
  cards.forEach((card) => {
    card.addEventListener("pointermove", (event) => {
      const bounds = card.getBoundingClientRect();
      const x = (event.clientX - bounds.left) / bounds.width;
      const y = (event.clientY - bounds.top) / bounds.height;
      const rotateY = (x - 0.5) * 4;
      const rotateX = (0.5 - y) * 4;

      card.style.transform = `perspective(1200px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) translateY(-2px)`;
    });

    card.addEventListener("pointerleave", () => {
      card.style.transform = "";
    });
  });
}

enableCardMotion();
updateAuthStatus();
