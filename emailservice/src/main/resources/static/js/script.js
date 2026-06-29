document.addEventListener("DOMContentLoaded", () => {
  const mailForm = document.getElementById("mailForm");
  const btnSubmit = document.getElementById("btnSubmit");
  const btnText = btnSubmit.querySelector(".btn-text");
  const loader = btnSubmit.querySelector(".loader");
  const formMessage = document.getElementById("formMessage");

  // Smooth Scroll
  document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
      e.preventDefault();

      document.querySelector(this.getAttribute("href")).scrollIntoView({
        behavior: "smooth",
      });
    });
  });

  // ENVIO DO FORMULÁRIO
  mailForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    // Loading
    btnText.textContent = "Enviando...";
    loader.classList.remove("hidden");
    btnSubmit.style.opacity = "0.7";
    btnSubmit.disabled = true;

    // Valores
    const body = {
      name: document.getElementById("name").value,
      senderEmail: document.getElementById("email").value,
      phone: document.getElementById("phone").value,
      message: document.getElementById("message").value,
    };

    try {
      const response = await fetch("/api/v1/emails/contact", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      });

      if (response.ok) {
        formMessage.className = "feedback success";
        formMessage.classList.remove("hidden");
        formMessage.innerHTML = `
          <i class="ph ph-check-circle"></i>
          E-mail enviado com sucesso!
        `;

        mailForm.reset();

        setTimeout(() => {
          formMessage.classList.add("hidden");
        }, 4000);
      } else {
        const errorData = await response.json();

        formMessage.className = "feedback error";
        formMessage.classList.remove("hidden");
        formMessage.innerHTML = `
          <i class="ph ph-warning-circle"></i>
          ${errorData.message || "Erro ao enviar e-mail"}
        `;

        setTimeout(() => {
          formMessage.classList.add("hidden");
        }, 4000);
      }
    } catch (error) {
      console.error("Erro ao enviar formulário:", error);

      formMessage.className = "feedback error";
      formMessage.classList.remove("hidden");
      formMessage.innerHTML = `
        <i class="ph ph-wifi-slash"></i>
        Erro de conexão com o servidor.
      `;

      setTimeout(() => {
        formMessage.classList.add("hidden");
      }, 4000);
    } finally {
      btnText.textContent = "Enviar mensagem";
      loader.classList.add("hidden");
      btnSubmit.style.opacity = "1";
      btnSubmit.disabled = false;
    }
  });
});