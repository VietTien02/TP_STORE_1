    const input1 = document.querySelector(".input-1");
    const eyeOpen1 = document.querySelector(".eye-open");
    const eyeClose1 = document.querySelector(".eye-close");
    eyeOpen1.addEventListener("click", function () {
    eyeOpen1.classList.add("hidden");
    eyeClose1.classList.remove("hidden");
    input1.setAttribute("type", "password");
});
    eyeClose1.addEventListener("click", function () {
    eyeOpen1.classList.remove("hidden");
    eyeClose1.classList.add("hidden");
    input1.setAttribute("type", "text");
});
    const input2 = document.querySelector(".input-2");
    const eyeOpen2 = document.querySelectorAll(".eye-open")[1];
    const eyeClose2 = document.querySelectorAll(".eye-close")[1];
    eyeOpen2.addEventListener("click", function () {
    eyeOpen2.classList.add("hidden");
    eyeClose2.classList.remove("hidden");
    input2.setAttribute("type", "password");
});
    eyeClose2.addEventListener("click", function () {
    eyeOpen2.classList.remove("hidden");
    eyeClose2.classList.add("hidden");
    input2.setAttribute("type", "text");
});
    const password1 = document.getElementById('password-1');
    const password2 = document.getElementById('password-2');
    const form = document.querySelector('form');

    form.addEventListener('submit', (e) => {
        if (password1.value.length < 8) {
            e.preventDefault();
            alert('Mật khẩu phải dài ít nhất 8 ký tự !');
        }
        if (password1.value !== password2.value) {
            e.preventDefault();
            alert('Mật khẩu không trùng khớp !');
        }
    });

