$(document).ready(function() {
    var remember = getCookie("username");
    if(remember != undefined && remember != ''){
        $('input[name=username]').val(remember);
        $("input.remember-me").attr("checked", true);
    }
});
$( "form" ).submit(function( event ) {
    var userName = $('input[name=username]').val();
    if ( $(".remember-me").is(":checked")){
        setCookie('username', userName, 30);
    } else {
        setCookie('username', undefined, 0);
    }
});

function setCookie(cname,cvalue,exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires=" + d.toGMTString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}


function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}
