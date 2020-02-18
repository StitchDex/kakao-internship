let guideEditor; // for users
let adminEditor; // for admin
let selectedData; //current guide_document id
let selectedText; //current guide_document title
let beforeContent;
let isEditing;
var hiddenNum;
var hidden = new Array();
var beforeTags = new Set();
var afterTags;
var beforeImageUrl = new Set();
var afterImageUrl = new Set();
var depth2Dir = new Array();
var documentKey;

function loadDoc(search_key) {
    let dockey = selectedData.substring(3, selectedData.length);

    if (search_key != null) {
        dockey = search_key;
    }
    if (!isNaN(dockey)) {
        if (window.location.pathname.startsWith("/admin")) {
            location.href = '/admin/document?doc_key=' + dockey;
        } else {
            location.href = '/guide/document?doc_key=' + dockey;
        }
    } else {
        console.log("document key error");
    }
}

//admin_edit_button click
function clickEditButton() {
    isEditing = !isEditing;
    isEditing ? $('#edit_button').text('취소') : location.reload();
    beforeContent = guideEditor.getData();
    beforeImageUrl = new Set(UrlParse(beforeContent));
    guideEditor.destroy(true);
    makeAdminEditor();

    $('select.select2-tagging').prop('disabled', false);
    $("#document-tag-list button.close").attr("disabled", false);
    $("#document-tag-list *").css("background-color", "#FFFFFF");
}

//admin edit_save_button click
function clickSaveButton() {
    const afterContent = adminEditor.getData();
    if(afterContent == beforeContent){
        alert("변경 내역이 없습니다.");
        return;
    }
    if (adminEditor == null) {
        alert("편집 버튼을 누른 후 저장버튼을 눌러주세요");
    } else {
        //Editor Save
        var dockey = documentKey;
        var recentUpdate = getGuideUpdate(dockey);
        var beforeUpdate = $('#guide-update').text();
        if (recentUpdate != beforeUpdate) {
            var reply = confirm("다른 작업자의 결과물과 충돌이 발생했습니다.\n현재문서를 저장할 경우 문제가 생길 수 있습니다.\n저장하시겠습니까?")
            if (reply == false) return;
        }

        adminEditor.set('isReadOnly', true);
        //+)check the doc is edit (if or editor method)

        var token = $("meta[name='_csrf']").attr("content");

        //IMAGE URL
        //Get before URL
        //Get after URL
        afterImageUrl = new Set(UrlParse(adminEditor.getData()));
        //Extract insertUrl & deleteUrl
        var inserUrl = substract(Array.from(afterImageUrl), Array.from(beforeImageUrl));
        var deleteUrl = substract(Array.from(beforeImageUrl), Array.from(afterImageUrl));
        //Insert URL to DB & Delete URL from DB
        var sendData = JSON.stringify({
            "id": dockey,
            "content": afterContent,
            "insertUrl": inserUrl,
            "deleteUrl": deleteUrl
        });

        $.ajax({
            url: '/admin/edit_doc',
            headers: {"X-CSRF-TOKEN": token},
            data: sendData,
            async: false,
            method: 'POST',
            dataType: 'html',
            contentType: 'application/json',
            // refresh page
            success: function (res) {
                if (res) {
                    setGuideUpdate($('#guide-title').text(), documentKey, "update");
                    setTags();
                } else {
                    alert("편집 에러");
                    location.reload();
                }
            }, error: function (error) {
                console.log(error);
                alert('저장 실패');
                location.reload();
            }
        });
    }

    function setTags() {
        //Tag Select2
        afterTags = new Set();
        $('#document-tag-list').children('div').each(function () {
            afterTags.add($(this).data('value'));
        });
        /*$.each($('select.select2-tagging option:selected'), function (key, val) {
            afterTags.add(val.text);
        });*/

        var insertTags = [];
        var deleteTags = [];

        insertTags = substract(Array.from(afterTags), Array.from(beforeTags));
        deleteTags = substract(Array.from(beforeTags), Array.from(afterTags));
        var issuc = false;
        $.ajax({
            'url': '/admin/updateTags',
            'contentType': 'application/json',
            'async': false,
            'data': JSON.stringify({'insert': insertTags, 'delete': deleteTags, 'doc_key': dockey}),
            'headers': {"X-CSRF-TOKEN": token},
            'method': 'POST',
            'success': function () {

                issuc = true;
            }
        });
        if (issuc) {
            beforeTags = new Set(afterTags);
        }
        alert("저장 성공");
        self.close();
        location.reload();
    }
}

//get guide_update on load
function getGuideUpdate(dockey) {
    var returnValue = "";
    $.ajax({
        url: '/guide/get_update?documentKey=' + dockey,
        async: false,
        method: 'GET',
        success: function (res) {
            returnValue = res;
            $("#guide-update").text(res);
        }, error: function (error) {
            alert("업데이트 내역 에러");
            console.log(error);
        }
    });
    return returnValue;
}

//set guide_update when save button clicked
function setGuideUpdate(title, key, type) {
    var sendData = JSON.stringify({"admin": $('#admin_name').val(), "documentKey": key, "title": title, "updateType": type});
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: '/admin/set_update',
        headers: {"X-CSRF-TOKEN": token},
        data: sendData,
        method: 'POST',
        dataType: 'html',
        contentType: 'application/json',
        success: function (res) {

        }, error: function (error) {
            console.log("업데이트 내역 오류");
        }
    });
}


function showGuideTag(ret) {
    $.each(ret, function (index, item) {
        $("#guide-tag").append("<a href='#'>" + item + "</a>");
    });
}


function deleteAdd(param) {
    $(param).parent("div").remove();
}

function initSelectTagging() {
    var tagList = [];
    var dockey = documentKey;

    //SELECT Tags FROM GUIDE_TAGGING WHERE (documentKey)
    $.ajax({
        'async': false,
        'url': '/guide/getTags',
        'data': {'doc_key': dockey},
        'success': function (data) {
            $.each(data, function (key, val) {
                //MAKE TAG LIST at admin-documnet.HTML
                var tagDiv = $('<div class="p-1 mr-1" style="border:1px solid darkgrey; border-radius: 3px" data-value="' + val.tag + '">' + val.tag + '<button class="close deleteTag" onclick="deleteAdd(this)"><span style="color: red" aria-hidden="true">×</span></button>' + '</div>');
                $('#document-tag-list').append(tagDiv);
                tagList.push(val.tag);
            });
            $("#document-tag-list button.close").attr("disabled", "disabled").off('click');
            $("#document-tag-list *").css("background-color", "#BABABA");
        }
    });

    window.location.pathname.startsWith("/guide") ? showGuideTag(tagList) :
        $('select.select2-tagging').select2(
            {
                'ajax': {
                    'url': '/admin/suggestTags',
                    'data': function (params) {
                        return {'tag': params.term};
                    },
                    'processResults': function (data) {
                        data = $.map(data, function (obj) {
                            obj.id = obj.id || obj.tag; // replace pk with your identifier
                            obj.text = obj.text || obj.tag; // replace pk with your identifier
                            return obj;
                        });
                        return {results: data};
                    }
                },
                'tags': true,
                'allowClear': true,
                'disabled': true,
                'tokenSeparators': [',', ' '], //태그 구분자 추가
                'createTag': function (params) { //태그 공백 제거
                    var term = $.trim(params.term);
                    if (term == '') {
                        return null;
                    } else {
                        return {'id': term, 'text': term, 'newTag': true};
                    }
                }
            }
        );
    beforeTags = new Set(tagList);
}

function substract(a, b) {
    return $(a).not(b).get();
}

function UrlParse(text) {
    var m,
        urls = [],
        str = text,
        rex = /[sS][rR][cC]\s*=\s*(?:'|")([^("|')]*)(?:'|")/g;

    while (m = rex.exec(str)) {
        urls.push(m[1]);
    }
    return urls;
}

function clickMainTag(tagName) {
    location.href = "/guide/search?tag=%23" + tagName;
}

function tagCheck(input) {
    var ret = true
    $('#document-tag-list').children('div').each(function () {
        if ($(this).data('value') == input) {
            ret = false;
        }
    });
    return ret; //중복 값 없음
}

$('#guide-tag').on('click', 'a', function (event) {
    let tag = event.target.text.substr(1);
    clickMainTag(tag);
});

$('.select2-tagging').on("select2-open", function () {
    $(this).select2('positionDropdown', true);
});

$('.select2-tagging').on('select2:select', function (e) {
    var val = e.params.data.text;
    if (tagCheck(val)) {
        var tagDiv = $('<div class="p-1 mr-1" style="border:1px solid darkgrey; border-radius: 3px" data-value="' + val + '">' + val + '<button class="close deleteTag" onclick="deleteAdd(this)"><span style="color: red" aria-hidden="true">×</span></button>' + '</div>');
        $('#document-tag-list').append(tagDiv);
    } else {
        alert("이미 포함되어 있는 태그 입니다.")
    }

    $('.select2-tagging').val(null).trigger('change');
});

$('.search-result-title').on('click', function () {
    var doc_key = $(this).attr('value');
    if (window.location.pathname.startsWith("/admin")) {
        location.href = '/admin/document?doc_key=' + doc_key;
    } else {
        location.href = '/guide/document?doc_key=' + doc_key;
    }
});

$('.search-result-tag').on('click', function () {
    var tag = $(this).attr('value');
    if (window.location.pathname.startsWith("/admin")) {
        location.href = "/admin/search?tag=" + encodeURIComponent(tag);
    } else {
        location.href = "/guide/search?tag=" + encodeURIComponent(tag);
    }
});
