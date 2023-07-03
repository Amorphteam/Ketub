//
//  Bridge.js
//  FolioReaderKit
//
//  Created by Heberti Almeida on 06/05/15.
//  Copyright (c) 2015 Folio Reader. All rights reserved.
//

var thisHighlight;
var audioMarkClass;
var wordsPerMinute = 200;

document.addEventListener("DOMContentLoaded", function(event) {
//    var lnk = document.getElementsByClassName("lnk");
//    for (var i=0; i<lnk.length; i++) {
//        lnk[i].setAttribute("onclick","return callVerseURL(this);");
//    }
});

// Generate a GUID
function guid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
    }
    var guid = s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
    return guid.toUpperCase();
}

// Get All HTML
function getHTML() {
    Highlight.getHtmlAndSaveHighlight(document.documentElement.outerHTML);
    //return document.documentElement.outerHTML;
}

// Class manipulation
function hasClass(ele,cls) {
  return !!ele.className.match(new RegExp('(\\s|^)'+cls+'(\\s|$)'));
}

function addClass(ele,cls) {
  if (!hasClass(ele,cls)) ele.className += " "+cls;
}

function removeClass(ele,cls) {
  if (hasClass(ele,cls)) {
    var reg = new RegExp('(\\s|^)'+cls+'(\\s|$)');
    ele.className=ele.className.replace(reg,' ');
  }
}

// Font name class
function setFontName(cls) {
    var elm = document.documentElement;
    removeClass(elm, "andada");
    removeClass(elm, "lato");
    removeClass(elm, "lora");
    removeClass(elm, "raleway");
    addClass(elm, cls);
}

// Toggle night mode
function nightMode(enable) {
    var elm = document.documentElement;
    if(enable) {
        addClass(elm, "nightMode");
    } else {
        removeClass(elm, "nightMode");
    }
}

// Toggle any background
function changeReaderColor(cls) {
    var elm = document.documentElement;
    removeClass(elm, "BASE");
    removeClass(elm, "LIGHT");
    removeClass(elm, "DARK");
    removeClass(elm, "CONTRAST");
    if(cls) {
        addClass(elm, cls);
    }
}

// Change font face
function changeTypeFace(cls) {
    var elm = document.documentElement;

    removeClass(elm, "FONT0");
    removeClass(elm, "FONT1");
    removeClass(elm, "FONT2");
    removeClass(elm, "FONT3");
    removeClass(elm, "FONT4");
    if(cls) {
        addClass(elm, cls);
    }

}

// Change font style
function changeFontStyle(cls){
    var elm = document.documentElement;
    if (cls.includes("fr")){
        removeClass(elm, "boldfr");
        removeClass(elm, "italicfr")

        addClass(elm, cls);

    }else if (cls.includes("pr")){
        removeClass(elm, "boldpr");
        removeClass(elm, "italicpr")

        addClass(elm, cls);

    }else{
        removeClass(elm, "bold");
        removeClass(elm, "italic")

        addClass(elm, cls);
    }


}

// Change font color
function changeFontColor(cls) {
    var elm = document.documentElement;
    if (cls.includes("fr")){
        removeClass(elm, "blackcssfr");
        removeClass(elm, "greencssfr")
        removeClass(elm, "greycssfr");
        removeClass(elm, "bluecssfr");
        removeClass(elm, "redcssfr");
        removeClass(elm, "whitecssfr");
        addClass(elm, cls);

    }else if (cls.includes("pr")){
        removeClass(elm, "blackcsspr");
        removeClass(elm, "greencsspr")
        removeClass(elm, "greycsspr");
        removeClass(elm, "bluecsspr");
        removeClass(elm, "redcsspr");
        removeClass(elm, "whitecsspr");
        addClass(elm, cls);

    }else{
        removeClass(elm, "blackcss");
        removeClass(elm, "greencss")
        removeClass(elm, "greycss");
        removeClass(elm, "bluecss");
        removeClass(elm, "redcss");
        removeClass(elm, "whitecss");
        addClass(elm, cls);
    }
}


// Show Or Hide Text
function showOrHideText(cls) {
    var elm = document.documentElement;
    if (cls.includes("fr")){
    removeClass(elm, "hideTxtfr");
    removeClass(elm, "showTxtfr");
    addClass(elm, cls);
    }else if (cls.includes("pr")){
        removeClass(elm, "hideTxtpr");
        removeClass(elm, "showTxtpr");
        addClass(elm, cls);

    }else {
        removeClass(elm, "hideTxt");
        removeClass(elm, "showTxt");
        addClass(elm, cls);
    }
}

// Show Or Hide Text
function showOrHideErab(cls) {
    var elm = document.documentElement;
    removeClass(elm, "hideErab");
    removeClass(elm, "showErab");
    addClass(elm, cls);
}

function setFontSize(cls) {
    var elm = document.documentElement;
        removeClass(elm, "SIZE0");
        removeClass(elm, "SIZE1");
        removeClass(elm, "SIZE2");
        removeClass(elm, "SIZE3");
        removeClass(elm, "SIZE4");
        addClass(elm, cls);
}

// Set LineHeight
function setLineHeight(cls) {
    var elm = document.documentElement;
    removeClass(elm, "SPACE0");
    removeClass(elm, "SPACE1");
    removeClass(elm, "SPACE2");
    removeClass(elm, "SPACE3");
    removeClass(elm, "SPACE4");
    addClass(elm, cls);
}

/*
 *	Native bridge Highlight text
 */
function highlightString(style) {
    var range = window.getSelection().getRangeAt(0);
    var selectionContents = range.extractContents();
    var elm = document.createElement("highlight");
    var id = guid();

    elm.appendChild(selectionContents);
    elm.setAttribute("id", id);
    elm.setAttribute("onclick","callHighlightURL(this);");
    elm.setAttribute("class", style);

    range.insertNode(elm);
    thisHighlight = elm;

    var params = [];
    params.push({id: id, rect: getRectForSelectedText(elm)});

    return JSON.stringify(params);
}

function getHighlightString(style) {
    var range = window.getSelection().getRangeAt(0);
    var selectionContents = range.extractContents();
    var elm = document.createElement("highlight");
    var id = guid();

    elm.appendChild(selectionContents);
    elm.setAttribute("id", id);
    elm.setAttribute("onclick","callHighlightURL(this);");
    elm.setAttribute("class", style);

    range.insertNode(elm);
    thisHighlight = elm;

    var params = [];
    params.push({id: id, rect: getRectForSelectedText(elm)});
    Highlight.getHighlightJson(JSON.stringify(params));
}

// Menu colors
function setHighlightStyle(style) {
    thisHighlight.className = style;
    Highlight.getUpdatedHighlightId(thisHighlight.id, style);
}

function removeThisHighlight() {
    thisHighlight.outerHTML = thisHighlight.innerHTML;
    Highlight.getRemovedHighlightId(thisHighlight.id);
}

function removeHighlightById(elmId) {
    var elm = document.getElementById(elmId);
    elm.outerHTML = elm.innerHTML;
    return elm.id;
}

function getHighlightContent() {
    return thisHighlight.textContent
}

function getBodyText() {
    return document.body.innerText;
}

// Method that returns only selected text plain
var getSelectedText = function() {
    return window.getSelection().toString();
}

// Method that gets the Rect of current selected text
// and returns in a JSON format
var getRectForSelectedText = function(elm) {
    if (typeof elm === "undefined") elm = window.getSelection().getRangeAt(0);

    var rect = elm.getBoundingClientRect();
    return "{{" + rect.left + "," + rect.top + "}, {" + rect.width + "," + rect.height + "}}";
}

// Method that call that a hightlight was clicked
// with URL scheme and rect informations
var callHighlightURL = function(elm) {
    var URLBase = "highlight://";
    var currentHighlightRect = getRectForSelectedText(elm);
    thisHighlight = elm;

    window.location = URLBase + encodeURIComponent(currentHighlightRect);
}


// Reading time
function getReadingTime() {
    var text = document.body.innerText;
    var totalWords = text.trim().split(/\s+/g).length;
    var wordsPerSecond = wordsPerMinute / 60; //define words per second based on words per minute
    var totalReadingTimeSeconds = totalWords / wordsPerSecond; //define total reading time in seconds
    var readingTimeMinutes = Math.round(totalReadingTimeSeconds / 60);

    return readingTimeMinutes;
}


function findElementWithID(node) {
    if( !node || node.tagName == "BODY")
        return null
    else if( node.id )
        return node
    else
        return findElementWithID(node)
}

function findElementWithIDInView() {

    if(audioMarkClass) {
        // attempt to find an existing "audio mark"
        var el = document.querySelector("."+audioMarkClass)

        // if that existing audio mark exists and is in view, use it
        if( el && el.offsetTop > document.body.scrollTop && el.offsetTop < (window.innerHeight + document.body.scrollTop))
            return el
    }

    // @NOTE: is `span` too limiting?
    var els = document.querySelectorAll("span[id]")

    for(indx in els){
        if( els[indx].offsetTop > document.body.scrollTop )
            return els[indx]
    }

    return null
}


/**
 Play Audio - called by native UIMenuController when a user selects a bit of text and presses "play"
 */
function playAudio() {
    var sel = getSelection();
    var node = null;

    // user selected text? start playing from the selected node
    if (sel.toString() != "") {
        node = sel.anchorNode ? findElementWithID(sel.anchorNode.parentNode) : null;

    // find the first ID'd element that is within view (it will
    } else {
        node = findElementWithIDInView()
    }

    playAudioFragmentID(node ? node.id : null)
}


/**
 Play Audio Fragment ID - tells page controller to begin playing audio from the following ID
 */
function playAudioFragmentID(fragmentID) {
    var URLBase = "play-audio://";
    window.location = URLBase + (fragmentID?encodeURIComponent(fragmentID):"")
}

/**
 Go To Element - scrolls the webview to the requested element
 */
function goToEl(el) {
    var top = document.body.scrollTop;
    var elTop = el.offsetTop - 20;
    var bottom = window.innerHeight + document.body.scrollTop;
    var elBottom = el.offsetHeight + el.offsetTop + 60

    if(elBottom > bottom || elTop < top) {
        document.body.scrollTop = el.offsetTop - 20
    }

    return el;
}

/**
 Remove All Classes - removes the given class from all elements in the DOM
 */
function removeAllClasses(className) {
    var els = document.body.getElementsByClassName(className)
    if( els.length > 0 )
    for( i = 0; i <= els.length; i++) {
        els[i].classList.remove(className);
    }
}

/**
 Audio Mark ID - marks an element with an ID with the given class and scrolls to it
 */
function audioMarkID(className, id) {
    if (audioMarkClass)
        removeAllClasses(audioMarkClass);

    audioMarkClass = className
    var el = document.getElementById(id);

    goToEl(el);
    el.classList.add(className)
}

function setMediaOverlayStyle(style){
    document.documentElement.classList.remove("mediaOverlayStyle0", "mediaOverlayStyle1", "mediaOverlayStyle2")
    document.documentElement.classList.add(style)
}

function setMediaOverlayStyleColors(color, colorHighlight) {
    var stylesheet = document.styleSheets[document.styleSheets.length-1];
    stylesheet.insertRule(".mediaOverlayStyle0 span.epub-media-overlay-playing { background: "+colorHighlight+" !important }")
    stylesheet.insertRule(".mediaOverlayStyle1 span.epub-media-overlay-playing { border-color: "+color+" !important }")
    stylesheet.insertRule(".mediaOverlayStyle2 span.epub-media-overlay-playing { color: "+color+" !important }")
}

function scrollToSearchResult(searchId) {
    var e = document.getElementById(searchId);
       if (!!e && e.scrollIntoView) {
           e.scrollIntoView();
           return "scrolled";
       }

    return "not scrolled";
}

function navigateToHashTag(hashTag) {
    window.location.hash = "";
    window.location.hash = hashTag;
}
