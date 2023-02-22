$("#submit").on('click', function (e) {
    e.preventDefault();
    const cardNumber = document.getElementById("cardNumber");
    const expirationDate = document.getElementById("expirationDate")
    const cvv2 = document.getElementById("cvv2");
    const captcha = document.getElementById("captcha");
    const orderId = document.getElementById("orderId");

    const form = new FormData();
    form.append("cardNumber", cardNumber.value);
    form.append("expirationDate", expirationDate.value);
    form.append("cvv2", cvv2.value);
    form.append("captcha", captcha.value);
    form.append("orderId", orderId.value);

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/order/pay-online",
        data: form,
        processData: false,
        contentType: false,
        dataType: "text",
        error: function () {
            e.preventDefault();
            alert("Error")
        },
        success: function () {
            e.preventDefault();
            alert("Success")
        },
        timeout: 6000
    });
})



