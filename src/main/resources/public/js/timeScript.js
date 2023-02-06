function timeControl() {
    // set timer for 4 min 40 sec, then ask user to confirm.
    setTimeout('userCheck()', 280000);
}
function userCheck() {
    // set page refresh for 20 sec
    var id=setTimeout('pageReload()', 20000);
    // If user selects "OK" the timer is reset
    // else the page will refresh from the server.
    if (confirm("This page is set to refresh in 20 seconds.Would you like more time?"))
        {
            clearTimeout(id);
            timeControl();
        }
}
function pageReload() {
    window.location.reload(true);
}
timeControl();