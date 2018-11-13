<%@include file="../commons/header.jsp" %>
<%@include file="../commons/navigation.jsp" %>
<div class="container">
    <div id="waiting" align="center">
        <h1>Waiting</h1>
        <h3>....</h3>
    </div>
    <div id="productAdded" align="center">
    </div>
</div>
<script type="text/javascript">
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
                    console.log(returnServiceUnavailable());
                    incrementCounter();
                    $('#waiting').hide();
                }
            });
        }
    }

    function returnServiceUnavailable() {
        if (COUNTER === 5) {
            $('#productAdded').html("<h3>SERVER IS DOWN</h3>");
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

</script>
<%@include file="../commons/footer.jsp" %>
