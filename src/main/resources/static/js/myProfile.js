document.getElementById("editButton").addEventListener("click", function () {
  document.getElementById("editButtons").style.display = "inline-block";
  this.style.display = "none";
});
document.getElementById("saveButton").addEventListener("click", function () {});
document.getElementById("cancelButton").addEventListener("click", function () {
  document.getElementById("editButtons").style.display = "none";
  document.getElementById("editButton").style.display = "inline-block";
});
// edit 1
document.getElementById("editButton1").addEventListener("click", function () {
  document.getElementById("editButtons1").style.display = "inline-block";
  this.style.display = "none";
});
document
  .getElementById("saveButton1")
  .addEventListener("click", function () {});
document.getElementById("cancelButton1").addEventListener("click", function () {
  document.getElementById("editButtons1").style.display = "none";
  document.getElementById("editButton1").style.display = "inline-block";
});

const editButton = document.getElementById("editButton2");
const fileInput = document.getElementById("fileInput2");
const saveButton = document.getElementById("saveButton2");
const cancelButton = document.getElementById("cancelButton2");
const currentImage = document.getElementById("currentImage2");
const editControls = document.getElementById("editControls2");

let currentImageURL = currentImage.src;
let editing = false;

editButton.addEventListener("click", function () {
  editing = true;
  editControls.style.display = "block";
  editButton.style.display = "none";
  fileInput.style.display = "none";
  currentImage.src = "https://www.topperstutors.com/img/upload.png"; // add this line to change the image source to the edit icon
});

cancelButton.addEventListener("click", function () {
  currentImage.src = currentImageURL;
  fileInput.style.display = "none";
  editControls.style.display = "none";
  editButton.style.display = "block";
  editing = false;
});

saveButton.addEventListener("click", function () {
  if (fileInput.files.length > 0) {
    const reader = new FileReader();
    reader.addEventListener("load", function () {
      currentImageURL = reader.result;
      currentImage.src = currentImageURL;
    });
    reader.readAsDataURL(fileInput.files[0]);
  }
  fileInput.style.display = "none";
  editControls.style.display = "none";
  editButton.style.display = "block";
  editing = false;
});

currentImage.addEventListener("click", function () {
  if (!editing) {
    return;
  }
  fileInput.click();
});

fileInput.addEventListener("change", function () {
  if (fileInput.files.length > 0) {
    const reader = new FileReader();
    reader.addEventListener("load", function () {
      currentImage.src = reader.result;
    });
    reader.readAsDataURL(fileInput.files[0]);
  }
});
  function onSubmitForm(event) {
    if (event.submitter && event.submitter.id === 'saveButton') {
      // Nếu người dùng nhấn nút "Lưu", cho phép gửi form đi
      return true;
    } else {
      // Ngăn chặn việc gửi form đi nếu người dùng không nhấn nút "Lưu"
      event.preventDefault();
      return false;
    }
  }
   function onSubmitForm2(event) {
    if (event.submitter && event.submitter.id === 'saveButton2') {
      // Nếu người dùng nhấn nút "Lưu", cho phép gửi form đi
      return true;
    } else {
      // Ngăn chặn việc gửi form đi nếu người dùng không nhấn nút "Lưu"
      event.preventDefault();
      return false;
    }
  }
