var COUNTER = 0;
const INTERVAL = 3000;
const MAXIMUM_ATTEMPTS = 5;

function callControllerViaAjax() {
    if (COUNTER < MAXIMUM_ATTEMPTS) {
        $.ajax({
            type: "POST",
            url: 'topics.html',
            data: {"uuid": "${suuid}"},
            success: function (data) {
                console.log(Object.keys(data).length);

                if (data !== "") {
                    setCounterMaximum();
                    console.log(data);
                    $('#productAdded').html(data);
                    $('#waiting').hide();
                } else {
                    incrementCounter();
                }
            }
            ,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                console.log("ERROR" + textStatus + errorThrown);
                incrementCounter();
            }
        });
    }
}

function setCounterMaximum() {
    COUNTER = MAXIMUM_ATTEMPTS;
    console.log("setCounterMaximum" + COUNTER);
}

function incrementCounter() {
    COUNTER++;
    console.log("incrementCounter" + COUNTER)
}

let intervalId = 0;
if (COUNTER < MAXIMUM_ATTEMPTS) {
    intervalId = setInterval(callControllerViaAjax, INTERVAL);
}