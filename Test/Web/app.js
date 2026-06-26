var API = "http://localhost:8080/taches";
var tachesFaites = new Set();
var filtreListe = "tous";
var filtreNav = "tous";
var toutesLesTaches = [];

window.onload = function () {
    var today = new Date().toISOString().split("T")[0];
    document.getElementById("add-date").value = today;

    document.getElementById("add-nom").onkeydown = function (e) {
        if (e.key === "Enter")
            ajouterTache();
    };

    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape")
            fermerModal();
    });

    chargerTaches();
};

// ── MODE SOMBRE ───────────────────────────────────
function toggleDark() {
    document.body.classList.toggle("dark");
    var btn = document.getElementById("btn-dark");
    var isDark = document.body.classList.contains("dark");
    btn.innerHTML = isDark
            ? '<i class="ti ti-sun"></i> Mode clair'
            : '<i class="ti ti-moon"></i> Mode sombre';
}

// ── AJOUTER ───────────────────────────────────────
function ajouterTache() {
    var nom = document.getElementById("add-nom").value.trim();
    var date = document.getElementById("add-date").value;
    var priorite = document.getElementById("add-priorite").value;

    if (!nom) {
        document.getElementById("add-nom").focus();
        return;
    }
    if (!date) {
        alert("Choisis une date !");
        return;
    }

    fetch(API, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({nom: nom, date: date, priorite: priorite})
    }).then(function () {
        document.getElementById("add-nom").value = "";
        chargerTaches();
    }).catch(function (err) {
        console.error("Erreur ajout:", err);
        alert("Serveur Java non démarré ?");
    });
}

// ── CHARGER ───────────────────────────────────────
function chargerTaches() {
    fetch(API)
            .then(function (res) {
                return res.json();
            })
            .then(function (data) {
                toutesLesTaches = data;
                afficherTaches();
                mettreAJourStats();
            })
            .catch(function (err) {
                console.error("Erreur:", err);
            });
}

// ── AFFICHER ──────────────────────────────────────
function afficherTaches() {
    var ul = document.getElementById("liste");
    ul.innerHTML = "";

    var visibles = toutesLesTaches.filter(function (t) {
        if (filtreNav === "faites")
            return tachesFaites.has(t.id);
        if (filtreNav === "actives")
            return !tachesFaites.has(t.id);
        if (filtreListe !== "tous")
            return t.priorite === filtreListe;
        return true;
    });

    var count = visibles.length;
    document.getElementById("tasks-count").textContent =
            count + " tâche" + (count > 1 ? "s" : "");

    if (visibles.length === 0) {
        ul.innerHTML =
                '<li class="empty-state">' +
                '<i class="ti ti-checklist"></i>' +
                '<p>Aucune tâche ici. Ajoutes-en une !</p>' +
                '</li>';
        return;
    }

    visibles.forEach(function (t) {
        var faite = tachesFaites.has(t.id);
        var li = document.createElement("li");
        li.className = "task-item" + (faite ? " done" : "");

        var cb = document.createElement("div");
        cb.className = "cb" + (faite ? " checked" : "");
        cb.setAttribute("title", faite ? "Décocher" : "Marquer faite");
        cb.onclick = function () {
            toggleFaite(t.id);
        };

        var name = document.createElement("div");
        name.className = "task-name";
        name.textContent = t.nom;

        var date = document.createElement("div");
        date.className = "task-date";
        date.textContent = t.date;

        var badge = document.createElement("span");
        badge.className = "prio-badge prio-" + t.priorite;
        var labels = {ELEVE: "Élevée", MOYEN: "Moyenne", BAS: "Basse"};
        badge.textContent = labels[t.priorite] || t.priorite;

        var actions = document.createElement("div");
        actions.className = "task-actions";

        var btnEdit = document.createElement("button");
        btnEdit.className = "icon-btn";
        btnEdit.setAttribute("aria-label", "Modifier");
        btnEdit.innerHTML = '<i class="ti ti-edit"></i>';
        btnEdit.onclick = function () {
            ouvrirModal(t);
        };

        var btnDel = document.createElement("button");
        btnDel.className = "icon-btn danger";
        btnDel.setAttribute("aria-label", "Supprimer");
        btnDel.innerHTML = '<i class="ti ti-trash"></i>';
        btnDel.onclick = function () {
            supprimerTache(t.id, li);
        };

        actions.appendChild(btnEdit);
        actions.appendChild(btnDel);

        li.appendChild(cb);
        li.appendChild(name);
        li.appendChild(date);
        li.appendChild(badge);
        li.appendChild(actions);
        ul.appendChild(li);
    });
}

// ── COCHER ────────────────────────────────────────
function toggleFaite(id) {
    if (tachesFaites.has(id)) {
        tachesFaites.delete(id);
    } else {
        tachesFaites.add(id);
    }
    afficherTaches();
    mettreAJourStats();
}

// ── SUPPRIMER ─────────────────────────────────────
function supprimerTache(id, li) {
    li.style.opacity = "0.4";
    fetch(API + "?id=" + id, {
        method: "DELETE",
        headers: {"Content-Type": "application/json"}
    }).then(function () {
        tachesFaites.delete(id);
        chargerTaches();
    }).catch(function (err) {
        li.style.opacity = "1";
        console.error("Erreur suppression:", err);
    });
}

// ── MODAL MODIFICATION ────────────────────────────
function ouvrirModal(t) {
    document.getElementById("modal-id").value = t.id;
    document.getElementById("modal-nom").value = t.nom;
    document.getElementById("modal-date").value = t.date;
    document.getElementById("modal-priorite").value = t.priorite;
    document.getElementById("modal").classList.add("open");
    document.getElementById("modal-nom").focus();
}

function fermerModal() {
    document.getElementById("modal").classList.remove("open");
}

function sauvegarder() {
    var id = parseInt(document.getElementById("modal-id").value);
    var nom = document.getElementById("modal-nom").value.trim();
    var date = document.getElementById("modal-date").value;
    var priorite = document.getElementById("modal-priorite").value;

    if (!nom || !date) {
        alert("Remplis tous les champs !");
        return;
    }

    fetch(API + "?id=" + id, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({nom: nom, date: date, priorite: priorite})
    }).then(function () {
        fermerModal();
        chargerTaches();
    }).catch(function (err) {
        console.error("Erreur modif:", err);
    });
}

// ── FILTRES ───────────────────────────────────────
function filtrerNav(type, el) {
    filtreNav = type;
    filtreListe = "tous";
    document.querySelectorAll(".nav-item").forEach(function (n) {
        n.classList.remove("active");
    });
    el.classList.add("active");
    var titres = {tous: "Toutes les tâches", faites: "Tâches complétées", actives: "En cours"};
    document.getElementById("titre-section").textContent = titres[type] || "Tâches";
    document.querySelectorAll(".filter-pill").forEach(function (p) {
        p.classList.remove("active");
    });
    document.querySelector(".filter-pill").classList.add("active");
    afficherTaches();
}

function filtrerListe(type, btn) {
    filtreListe = type;
    document.querySelectorAll(".filter-pill").forEach(function (b) {
        b.classList.remove("active");
    });
    btn.classList.add("active");
    afficherTaches();
}

// ── STATS ─────────────────────────────────────────
function mettreAJourStats() {
    var total = toutesLesTaches.length;
    var faites = tachesFaites.size;
    var actives = total - faites;
    var eleve = toutesLesTaches.filter(function (t) {
        return t.priorite === "ELEVE" && !tachesFaites.has(t.id);
    }).length;

    document.getElementById("s-total").textContent = total;
    document.getElementById("s-faites").textContent = faites;
    document.getElementById("s-actives").textContent = actives;
    document.getElementById("s-eleve").textContent = eleve;

    document.getElementById("count-tous").textContent = total;
    document.getElementById("count-faites").textContent = faites;
    document.getElementById("count-actives").textContent = actives;
}