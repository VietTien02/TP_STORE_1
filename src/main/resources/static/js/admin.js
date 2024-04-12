
    var x, i, j, l, ll, selElmnt, a, b, c;
    /*look for any elements with the class "custom-select":*/
    x = document.getElementsByClassName("custom-select");
    l = x.length;
    for (i = 0; i < l; i++) {
    selElmnt = x[i].getElementsByTagName("select")[0];
    ll = selElmnt.length;
    /*for each element, create a new DIV that will act as the selected item:*/
    a = document.createElement("DIV");
    a.setAttribute("class", "select-selected");
    a.innerHTML = selElmnt.options[selElmnt.selectedIndex].innerHTML;
    x[i].appendChild(a);
    /*for each element, create a new DIV that will contain the option list:*/
    b = document.createElement("DIV");
    b.setAttribute("class", "select-items select-hide");
    for (j = 1; j < ll; j++) {
    /*for each option in the original select element,
    create a new DIV that will act as an option item:*/
    c = document.createElement("DIV");
    c.innerHTML = selElmnt.options[j].innerHTML;
    c.addEventListener("click", function(e) {
    /*when an item is clicked, update the original select box,
    and the selected item:*/
    var y, i, k, s, h, sl, yl;
    s = this.parentNode.parentNode.getElementsByTagName("select")[0];
    sl = s.length;
    h = this.parentNode.previousSibling;
    for (i = 0; i < sl; i++) {
    if (s.options[i].innerHTML == this.innerHTML) {
    s.selectedIndex = i;
    h.innerHTML = this.innerHTML;
    y = this.parentNode.getElementsByClassName("same-as-selected");
    yl = y.length;
    for (k = 0; k < yl; k++) {
    y[k].removeAttribute("class");
}
    this.setAttribute("class", "same-as-selected");
    break;
}
}
    h.click();
});
    b.appendChild(c);
}
    x[i].appendChild(b);
    a.addEventListener("click", function(e) {
    /*when the select box is clicked, close any other select boxes,
    and open/close the current select box:*/
    e.stopPropagation();
    closeAllSelect(this);
    this.nextSibling.classList.toggle("select-hide");
    this.classList.toggle("select-arrow-active");
});
}
    function closeAllSelect(elmnt) {
    /*a function that will close all select boxes in the document,
    except the current select box:*/
    var x, y, i, xl, yl, arrNo = [];
    x = document.getElementsByClassName("select-items");
    y = document.getElementsByClassName("select-selected");
    xl = x.length;
    yl = y.length;
    for (i = 0; i < yl; i++) {
    if (elmnt == y[i]) {
    arrNo.push(i)
} else {
    y[i].classList.remove("select-arrow-active");
}
}
    for (i = 0; i < xl; i++) {
    if (arrNo.indexOf(i)) {
    x[i].classList.add("select-hide");
}
}
}
    var activeSpan = null;

    function activate(span) {
    if (activeSpan) {
    activeSpan.classList.remove("active");
}
    span.classList.add("active");
    activeSpan = span;
}
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
    function openCity(cityName) {
    var i;
    var x = document.getElementsByClassName("city");
    for (i = 0; i < x.length; i++) {
    x[i].style.display = "none";
}
    document.getElementById(cityName).style.display = "block";
}

    var btnContainer = document.getElementById("w33");
    var btns = btnContainer.getElementsByClassName("w3-button");
    for (var i = 0; i < btns.length; i++) {
    btns[i].addEventListener("click", function () {
        var current = document.getElementsByClassName("active");
        current[0].className = current[0].className.replace(" active", "");
        this.className += " active";
    });
}
    function checkScreenWidth() {
    var screenWidth = window.innerWidth;
    var Fullscreen = document.getElementsByClassName("admin-qltk");
    if (screenWidth < 1225) {
    Fullscreen.style.display = "none";
} else {
    Fullscreen.style.display = "block";
}
}
    window.onload = checkScreenWidth;
    window.onresize = checkScreenWidth;
