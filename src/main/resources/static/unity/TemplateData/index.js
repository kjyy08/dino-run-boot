document.addEventListener("DOMContentLoaded", async () => {
    AOS.init();

    let receiveScore = 0;

    document.querySelector(".score-form").addEventListener("submit", async (event) => {
        event.preventDefault();
        await fetchScore();
    });

    async function fetchScore() {
        const playerName = document.getElementById("player-name").value;
        const modal = new bootstrap.Modal(document.getElementById("scoreModal"));
        const modalTitle = document.getElementById("scoreModalLabel");
        const modalBody = document.getElementById("scoreModalBody");
        const modalButton = document.getElementById("scoreModalButton");

        if (!receiveScore || receiveScore === 0) {
            modalTitle.innerText = "⚠️ 경고";
            modalBody.innerText = "먼저 게임을 플레이하고 점수를 획득해주세요!";
            modalButton.classList.replace("btn-primary", "btn-secondary");
            modal.show();
            return;
        }

        await fetch(`/api/scores`, {
            method: "POST",
            headers: {"Content-type": "application/json"},
            body: JSON.stringify({nickname: playerName, point: receiveScore}),
        });

        modalTitle.innerText = "🎉 제출 완료!";
        modalBody.innerText = `${playerName}님의 점수가 성공적으로 등록되었습니다!`;
        modalButton.classList.replace("btn-secondary", "btn-primary");

        modalButton.onclick = () => {
            document.getElementById("player-name").value = "";
            receiveScore = 0;
        };

        modal.show();
        sampleRankings = await fetchRankings();
        loadRankingTable();
    }

    // Sample ranking data for demonstration
    let sampleRankings = await fetchRankings();

    function loadRankingTable() {
        let idx = 1;
        const rankingBody = document.getElementById("ranking-body");
        rankingBody.innerHTML = ``;

        sampleRankings.forEach((ranking) => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${idx++}</td>
                <td>${ranking.nickname}</td>
                <td>${ranking.point}</td>
            `;
            rankingBody.appendChild(row);
        });
    }

    // loadRankingTable();

    async function fetchRankings() {
        try {
            const response = await fetch(`/api/scores`);
            const data = await response.json();

            console.log("Rankings: " + data);
            return data;
        } catch (e) {
            console.error(e);
            return null;
        }
    }

    window.receiveHighScore = function (score) {
        receiveScore = score;
        console.log("High Score received from Unity:", receiveScore);
    };

    /*
        Unity Code Section
     */
    var canvas = document.querySelector("#unity-canvas");

    function unityShowBanner(msg, type) {
        var warningBanner = document.querySelector("#unity-warning");

        function updateBannerVisibility() {
            warningBanner.style.display = warningBanner.children.length
                ? "block"
                : "none";
        }

        var div = document.createElement("div");
        div.innerHTML = msg;
        warningBanner.appendChild(div);
        if (type == "error")
            div.style = "background: red; padding: 10px; border-radius: 4px;";
        else {
            if (type == "warning")
                div.style = "background: yellow; padding: 10px; border-radius: 4px;";
            setTimeout(function () {
                warningBanner.removeChild(div);
                updateBannerVisibility();
            }, 5000);
        }
        updateBannerVisibility();
    }

    var buildUrl = "/unity/Build";
    var loaderUrl = buildUrl + "/Build.loader.js";
    var config = {
        arguments: [],
        dataUrl: buildUrl + "/Build.data",
        frameworkUrl: buildUrl + "/Build.framework.js",
        codeUrl: buildUrl + "/Build.wasm",
        streamingAssetsUrl: "StreamingAssets",
        companyName: "juyb99",
        productName: "dino-run-client",
        productVersion: "1.0",
        showBanner: unityShowBanner,
    };

    if (/iPhone|iPad|iPod|Android/i.test(navigator.userAgent)) {
        var meta = document.createElement("meta");
        meta.name = "viewport";
        meta.content =
            "width=device-width, height=device-height, initial-scale=1.0, user-scalable=no, shrink-to-fit=yes";
        document.getElementsByTagName("head")[0].appendChild(meta);
        document.querySelector("#unity-container").className = "unity-mobile";
        canvas.className = "unity-mobile";
    } else {
        canvas.style.width = "1080px";
        canvas.style.height = "640px";
    }

    document.querySelector("#unity-loading-bar").style.display = "block";

    var script = document.createElement("script");
    script.src = loaderUrl;
    script.onload = () => {
        createUnityInstance(canvas, config, (progress) => {
            document.querySelector("#unity-progress-bar-full").style.width =
                100 * progress + "%";
        })
            .then((unityInstance) => {
                document.querySelector("#unity-loading-bar").style.display = "none";
            })
            .catch((message) => {
                alert(message);
            });
    };

    document.body.appendChild(script);
});
