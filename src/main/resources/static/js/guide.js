//for ckeditor
let doc_editor; // for users
let admin_editor; // for admin
let selectedData; //current guide_document id
let selectedText; //current guide_document title
var hidden_num;
var hidden = new Array();
var beforeTags = new Set();
var afterTags;
var beforeImageUrl = new Set();
var afterImageUrl = new Set();
var depth2Dir = new Array();
var depth2Dir_num;
var documentKey;
let cur;
//document ready
$(function () {
    hidden_num = 0;
    depth2Dir = 0;
    documentKey = $('#selected').val();

    $('#jstree').jstree({
        'core': {
            'multiple': false,
            "check_callback": true,
            'themes': {
                'icon': true,
                'responsive': true
            },
            'data': {
                'url': '/guide/tree',
                'type': "GET",
                'dataType': 'json',
                'data': function (node) {
                    return {"id": node.id == "#" ? "IAM" : node.id};
                },
                'success': function (data) {
                    for (i = 0; i < data.length; i++) {
                        var temp = (data[i].state);
                        if (!temp) {
                            hidden[hidden_num++] = data[i].id;
                        }
                    }
                    console.log(data);
                }
            }
        },
        'types': {
            "DIR": {
                "icon": "far fa-folder"
            },
            "DOC": {
                "icon": "far fa-file",
                "max_children": 0,
            },
        },
        'plugins': ["types", "state", "wholerow"]
    }).delegate("a", "dblclick", function () {
        $(this).parents(".jstree:eq(0)").jstree("toggle_node", this);
    }).on('ready.jstree', function () {
        before_tree_open($(this).jstree('close_all'), $(this).jstree('open_node', 'DIR0'));
        cur = $('#jstree').jstree('get_node', 'DOC' + documentKey);
        if (documentKey != null) {
            select_open();
        }
    });
    if (documentKey == null) { // admin_main, guide_main
        return;
    }

    $.ajax({
        'url': '/guide/menu',
        'data': {'doc_key': documentKey},
        'success': function (res) {
            var title = res.title;
            $('#guide-title').text(title);
            if (admin_editor != null) { // change doc while edit
                admin_editor.destroy(true);
                make_editor(res.text);
            } else if (doc_editor != null) { // change doc
                doc_editor.destroy();
                make_editor(res.text);
            } else { // document_ready
                make_editor(res.text);
            }
            init_select_tagging();
            $('#guide-update').text( get_Guide_update(documentKey));
        },
        'error': function () {

        },
    });
});

//click tree_node
$('#jstree').on('select_node.jstree', function (e, data) {
    selectedData = data.node.id;
    selectedText = data.node.text;

    //click dir_node
    if (selectedData.startsWith("DIR")) {
        data.node.state.opened ? $(this).jstree('close_node', selectedData)
            : $(this).jstree('open_node', selectedData);
    }
    //click page_node
    else {
        $('#jstree').jstree('clear_state');
        loadDoc();
    }
});

function select_open() {
    while (cur.id != "DIR0") {
        $('#jstree').jstree('open_node', cur.parent);
        let cur_p = cur.parent;
        cur = $('#jstree').jstree('get_node', cur_p);
    }
}

function before_tree_open() {
    for (var i = 0; i < hidden.length; i++) {
        $("#jstree").jstree(true).hide_node(hidden[i]);
    }
}

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


//make user editor and set html data
function make_editor(res) {
    ClassicEditor
        .create(document.querySelector('#Guide_Doc'),
        )
        .then(editor => {
            editor.set('isReadOnly', true);
            doc_editor = editor;
            doc_editor.setData(res);
        })
        .catch(error => {
                console.error(error);
            }
        );

}

//admin_edit_button click
function edit_button_click() {
    var temp = doc_editor.getData();
    beforeImageUrl = new Set(UrlParse(temp));
    doc_editor.destroy(true);
    ClassicEditor
        .create(document.querySelector('#Guide_Doc'), {
                extraPlugins: [MyCustomUploadAdapterPlugin],
                toolbar: ["bold", "heading", "imageTextAlternative", "imageStyle:full", "imageUpload", "indent", "outdent",
                    "italic", "link", "numberedList", "bulletedList", "insertTable", "tableColumn", "tableRow", "mergeTableCells", "alignment:left",
                    "alignment:right", "alignment:center", "alignment:justify", "alignment", "fontSize", "underline", "undo", "redo"],
                image: {
                    toolbar: ['imageTextAlternative', '|', 'imageStyle:alignLeft', 'imageStyle:full', 'imageStyle:alignRight'],
                    styles: [
                        'full',
                        'alignLeft',
                        'alignRight'
                    ]
                },
                heading: {
                    options: [
                        {model: 'paragraph', title: 'Paragraph', class: 'ck-heading_paragraph'},
                        {model: 'heading1', view: 'h1', title: 'Heading 1', class: 'ck-heading_heading1'},
                        {model: 'heading2', view: 'h2', title: 'Heading 2', class: 'ck-heading_heading2'},
                        {model: 'heading3', view: 'h3', title: 'Heading 3', class: 'ck-heading_heading3'}
                    ]
                }
            }
        )
        .then(editor => {
            admin_editor = editor;
        })
        .catch(error => {
                alert("편집 버튼 에디터 오류");
            }
        );

    $('select.select2-tagging').prop('disabled', false);
    $("#document-tag-list button.close").attr("disabled", false);
    $("#document-tag-list *").css("background-color", "#FFFFFF");
}

//admin edit_save_button click
function edit_save_button_click() {
    if (admin_editor == null) {
        alert("Error");
    } else {
        //Editor Save
        var dockey = documentKey;
        var recentUpdate = get_Guide_update(dockey);
        var beforeUpdate = $('#guide-update').text();
        if(recentUpdate != beforeUpdate) {
            var reply = confirm("다른 작업자의 결과물과 충돌이 발생했습니다. 현재문서를 저장할 경우 문제가 생길 수 있습니다.\n 저장하시겠습니까?")
            if(reply == false) return;
        }

        admin_editor.set('isReadOnly', true);
        //+)check the doc is edit (if or editor method)
        const edit_doc = admin_editor.getData();
        var token = $("meta[name='_csrf']").attr("content");
        //IMAGE URL
        //Get before URL
        //Get after URL
        afterImageUrl = new Set(UrlParse(admin_editor.getData()));
        //Extract insertUrl & deleteUrl
        var inserUrl = substract(Array.from(afterImageUrl), Array.from(beforeImageUrl));
        var deleteUrl = substract(Array.from(beforeImageUrl), Array.from(afterImageUrl));
        //Insert URL to DB & Delete URL from DB
        var sendData = JSON.stringify({
            "id": dockey,
            "content": edit_doc,
            "insertUrl": inserUrl,
            "deleteUrl": deleteUrl
        });

        $.ajax({
            url: '/admin/edit_doc',
            headers: {"X-CSRF-TOKEN": token},
            data: sendData,
            method: 'POST',
            dataType: 'html',
            contentType: 'application/json',
            // refresh page
            success: function (res) {
                res ? set_Guide_update($('#guide-title').text(), 'update') : alert('error');
            }, error: function (error) {
                alert('저장 실패');
            }
        });

        //Tag Select2
        afterTags = new Set();
        $('#document-tag-list').children('div').each(function () {
            afterTags.add($(this).data('value'));
        })
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
        alert("save ok");
        self.close();
        location.reload();
    }
}

//get guide_update when node is selected
function get_Guide_update(dockey) {
    var returnValue = "";
    console.log(dockey);
    $.ajax({
        url: '/guide/get_update?documentKey=' + dockey,
        async : false,
        method: 'GET',
        success: function (res) {
            returnValue = res;
        }, error: function (error) {
            console.log(error);
        }
    });

    return returnValue;
}

//set guide_update when save button clicked
function set_Guide_update(title, type) {
    var sendData = JSON.stringify({"admin": $('#admin_name').val(), "documentKey" : documentKey, "title": title, "CRUD": type});
    console.log(sendData);
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: '/admin/set_update',
        headers: {"X-CSRF-TOKEN": token},
        data: sendData,
        method: 'POST',
        dataType: 'html',
        contentType: 'application/json',
        success: function (res) {
            console.log("update");
        }, error: function (error) {
            console.log(error);
        }
    });
}
$('#guide-tag').on('click','a',function(event){
    let tag = event.target.text.substr(1);
    menu_tag(tag);
});
function show_guide_tag(ret) {
    let temp = "";
    $.each(ret, function (index, item) {
        $("#guide-tag").append("<a href='#'>"+item+"</a>");
    });
}

$('.select2-tagging').on("select2-open",function () {
    $(this).select2('positionDropdown', true);
})

function deleteAdd(param){
    $(param).parent("div").remove();
}

function init_select_tagging() {
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
                var tagDiv = $('<div class="p-1 mr-1" style="border:1px solid darkgrey; border-radius: 3px" data-value="' + val.tag + '">' + val.tag + '<button class="close deleteTag" onclick="deleteAdd(this)"><span style="color: red" aria-hidden="true">×</span></button>' +'</div>');
                $('#document-tag-list').append(tagDiv);
                tagList.push(val.tag);
            });
            $("#document-tag-list button.close").attr("disabled", "disabled").off('click');
            $("#document-tag-list *").css("background-color", "#BABABA");
        }
    });

    window.location.pathname.startsWith("/guide") ? show_guide_tag(tagList) :
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

function menu_tag(tag_name) {
    location.href = "/guide/search?tag=%23" + tag_name;
}

$('.select2-tagging').on('select2:select',function (e) {
    var val = e.params.data.text;
    if(tagCheck(val)) {
        var tagDiv = $('<div class="p-1 mr-1" style="border:1px solid darkgrey; border-radius: 3px" data-value="' + val + '">' + val + '<button class="close deleteTag" onclick="deleteAdd(this)"><span style="color: red" aria-hidden="true">×</span></button>' +'</div>');
        $('#document-tag-list').append(tagDiv);
    }
    else {
        alert("이미 포함되어 있는 태그 입니다.")
    }

    $('.select2-tagging').val(null).trigger('change');
})

function tagCheck(input){
    var ret = true
    $('#document-tag-list').children('div').each(function (index) {
        if($(this).data('value')==input) {
            ret = false;
        }
    })
    return ret; //중복 값 없음
}

$('.search-result-item').on('click', function () {
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
