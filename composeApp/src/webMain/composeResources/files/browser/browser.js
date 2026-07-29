(function () {
    const homeView = document.getElementById("home-view");
    const resultsView = document.getElementById("results-view");
    const form = document.getElementById("search-form");
    const input = document.getElementById("search-input");
    const askedQuestion = document.getElementById("asked-question");
    const backLink = document.getElementById("back-link");
    const container = document.getElementById("page-container");

    function showResults(query) {
        askedQuestion.textContent = query || "...nothing?";
        homeView.hidden = true;
        resultsView.hidden = false;
        container.scrollTo({ top: 0 });
    }

    function showHome() {
        homeView.hidden = false;
        resultsView.hidden = true;
        container.scrollTo({ top: 0 });
    }

    form.addEventListener("submit", (e) => {
        e.preventDefault();
        showResults(input.value.trim());
    });

    document.querySelectorAll(".try-btn").forEach((btn) => {
        btn.addEventListener("click", () => {
            input.value = btn.textContent;
            showResults(btn.textContent);
        });
    });

    backLink.addEventListener("click", () => {
        input.value = "";
        showHome();
    });

    // Cosmetic fake counter, purely for flavor
    const statsLine = document.getElementById("stats-line");
    let answered = 2412987;
    setInterval(() => {
        answered += Math.floor(Math.random() * 3);
        statsLine.textContent = `Engine has answered ${answered.toLocaleString()} questions today.`;
    }, 4000);
})();