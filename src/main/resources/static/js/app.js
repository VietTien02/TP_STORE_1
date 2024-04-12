let searchBtn = document.querySelector(".searchBtn");
let closeBtn = document.querySelector(".closeBtn");
let searchBox = document.querySelector(".searchBox");
let navigation = document.querySelector(".navigation");
let menuToggle = document.querySelector(".menuToggle");
let header = document.querySelector("header");
searchBtn.onclick = function () {
    searchBox.classList.add("active");
    closeBtn.classList.add("active");
    searchBtn.classList.add("active");
    menuToggle.classList.add("hide");
    header.classList.remove("open");
};
closeBtn.onclick = function () {
    searchBox.classList.remove("active");
    closeBtn.classList.remove("active");
    searchBtn.classList.remove("active");
    menuToggle.classList.remove("hide");
};
menuToggle.onclick = function () {
    header.classList.toggle("open");
    searchBox.classList.remove("active");
    closeBtn.classList.remove("active");
    searchBtn.classList.remove("active");
};
const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);

const tabs = $$(".tab-item");
const panes = $$(".tab-pane");

const tabActive = $(".tab-item.active");
const line = $(".tabs .line");

// SonDN fixed - Active size wrong size on first load.
// Original post: https://www.facebook.com/groups/649972919142215/?multi_permalinks=1175881616551340
requestIdleCallback(function () {
    line.style.left = tabActive.offsetLeft + "px";
    line.style.width = tabActive.offsetWidth + "px";
});

tabs.forEach((tab, index) => {
    const pane = panes[index];

    tab.onclick = function () {
        $(".tab-item.active").classList.remove("active");
        $(".tab-pane.active").classList.remove("active");

        line.style.left = this.offsetLeft + "px";
        line.style.width = this.offsetWidth + "px";

        this.classList.add("active");
        pane.classList.add("active");
    };
});
const accordion = document.getElementsByClassName("contentBx");
for (i = 0; i < accordion.length; i++) {
  accordion[i].addEventListener("click", function () {
    this.classList.toggle("active");
  });

  const content = accordion[i].getElementsByClassName("accordion-content")[0];
  content.addEventListener("click", function (event) {
    event.stopPropagation();
  });
}
var modal = document.getElementById("myModal");
// Get the button that opens the modal
var btn = document.getElementById("myBtn");
// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];
//When the user clicks the button, open the modal
btn.onclick = function () {
    modal.style.display = "block";
};
// When the user clicks on <span> (x), close the modal
span.onclick = function () {
    modal.style.display = "none";
};
// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
};
document.getElementById("submitBtn").addEventListener("click", function() {
    document.getElementById("myForm").submit();
});

//kich hoat khoa hoc
    function submitForm() {
        var inputElement = document.getElementById("code");
        var code = inputElement.value;
        if (code == null || code.trim().length == 0) {
            // Hiển thị lỗi cho người dùng biết
            alert("Vui lòng nhập mã kích hoạt");
            return;
        }

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/kichhoatkhoahoc", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onload = function () {
            // Xử lý kết quả trả về từ controller
            var response = xhr.responseText;
            if (response == "success") {
                // Nếu kích hoạt khóa học thành công
                alert("Kích hoạt khóa học thành công");
                // Đóng modal

                modal.style.display = "none";
            } else if (response == "invalid") {
                alert("Mã code không hợp lệ");
            } else if (response == "invalid1") {
                alert("Mã code Đã Hết !");
            } else if (response == "used") {
                alert("Mã code đã được sử dụng");
            }

        };
        xhr.send("code=" + code);
    }
function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}
window.onclick = function (event) {
    if (!event.target.matches(".dropbtn")) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains("show")) {
                openDropdown.classList.remove("show");
            }
        }
    }
};





