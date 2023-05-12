
jsThemeOriginal = null;
jsTheme_2 = null;
jsTheme_3 = null;
jsTheme_4 = null;

$(function() {

    $(document).ready(function(){
    $(window).load(function()
    {

    });
    });

    // change theme to default style
    jqThemeOriginal = function()
    {
        $("*").css('color', '');
        $("*").css('background-color', '');
    };

    jsThemeOriginal = jqThemeOriginal;
    //--------

    // change color theme 2
    jqTheme_2 = function()
    {
        $("*").css('color', '');
        $("*").css('background-color', '#ffffce');
    };

    jsTheme_2 = jqTheme_2
    //--------

    // change color theme 3
    jqTheme_3 = function()
    {
        $("*").css('color', '#ffffff');
        $("*").css('background-color', '#202020');
    };

    jsTheme_3 = jqTheme_3
    //--------

    // change color theme 4
    jqTheme_4 = function()
    {
        $("*").css('color', '');
        $("*").css('background-color', '#cacac9');
    };

    jsTheme_4 = jqTheme_4
    //--------
});
