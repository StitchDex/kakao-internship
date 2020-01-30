//for ckeditor
let doc_editor; // for users
let admin_editor; // for admin
let isReadOnly;
let selectedData; //current guide_document id
let selectedText; //current guide_document title
var hidden_num;
var hidden = new Array();
var beforeTags = new Set();
var afterTags;
//document ready
$(function () {
    hidden_num=0;
    isReadOnly = false;
    var searched = $('#selected').val();

    $('#jstree').jstree({
        'core': {
            'multiple': false,
            "check_callback": true,
            'themes': {
                'icon' : true,
                'responsive': true
            },
            'data':  {
                'url' : '/guide/tree',
                'type': "GET",
                'dataType' : 'json',
                'data' : function(node){
                    return {"id": node.id == "#" ? "IAM" : node.id };
                },
                'success': function (data) {
                    for(i=0;i<data.length;i++){
                        var temp=(data[i].state);
                        if(!temp) {
                            hidden[hidden_num++] = data[i].id;
                        }
                    }
                    console.log(data);
                }
             }
        },
        'types' : {
            "DIR": {
                "icon" : "far fa-folder"
            },
            "DOC": {
                "icon" : "far fa-file",
                "max_children" : 0,
            },
        },
        'plugins' : ["wholerow","types", "state"]
    })
        .on('ready.jstree', function(){
            make_hide();
            $('#jstree').jstree('select_node', "DOC" + searched);
        $(this).jstree('open_all')
    });

    $('#jstree').jstree('clear_state');
    console.log("BP")
});

//click tree_node
$('#jstree').on('select_node.jstree', function (e, data) {
    selectedData = data.node.id;
    selectedText = data.node.text;
    //click dir_node
    if(selectedData.startsWith("DIR")){
        //no event
    }
    //click page_node
    else {
        LoadDocText();
    }
});

function LoadDocText() {
    var dockey = selectedData.substring(3, selectedData.length);
    if (!isNaN(dockey)) {
        $.ajax({
            url: '/guide/menu?doc_key=' + dockey,
            method: 'GET',
            success: function (res) {//set DOCUMENT_TEXT in editor area
                var title = res.title;
                res = res.text;
                $('#guide-title').text(title);
                get_Guide_update(selectedText);
                if(admin_editor!=null){ // change doc while edit
                    admin_editor.destroy();
                    make_editor(res);
                }
                else if(doc_editor!=null){ // change doc
                    doc_editor.destroy();
                    make_editor(res);
                }
                else{ // document_ready
                    make_editor(res);
                }
            }, error: function (error) {
                console.log(error);
            }
        });
        init_select_tagging();
    } else {
        console.log("not document");
    }
}

function make_hide() {
    for(var i=0;i<hidden.length;i++) {
        $("#jstree").jstree(true).hide_node(hidden[i]);
    }
}
//make user editor and set html data
function make_editor(res){
    ClassicEditor
        .create(document.querySelector('#Guide_Doc'),
        )
        .then(editor => {
            editor.set('isReadOnly',true);
            doc_editor=editor;
            doc_editor.setData(res);
        })
        .catch(error => {
                console.error(error);
            }
        );
}

//admin_edit_button click
function edit_button_click() {
    isReadOnly = false;
    doc_editor.destroy(true);
    ClassicEditor
        .create( document.querySelector( '#Guide_Doc' ), {
                extraPlugins:[MyCustomUploadAdapterPlugin],

                toolbar: ["bold", "heading","imageTextAlternative","imageStyle:full", "imageUpload", "indent", "outdent",
                    "italic", "link", "numberedList", "bulletedList", "insertTable", "tableColumn", "tableRow", "mergeTableCells", "alignment:left",
                    "alignment:right", "alignment:center", "alignment:justify", "alignment", "fontSize", "underline", "undo", "redo"],
                image: {
                    toolbar: [ 'imageTextAlternative', '|', 'imageStyle:alignLeft', 'imageStyle:full', 'imageStyle:alignRight' ],
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
        .then( editor => {
            admin_editor=editor;
        })
        .catch( error => {
                console.error( error );
            }
        );
    $('select.select2-tagging').prop('disabled', isReadOnly);
}

//admin edit_save_button click
function edit_save_button_click() {
    if (admin_editor == null) {
        alert("Error");
    } else {
        //Editor Save
        var dockey = selectedData.substring(3, selectedData.length);
        admin_editor.set('isReadOnly', true);
        //+)check the doc is edit (if or editor method)
        const edit_doc = admin_editor.getData();
        var sendData = JSON.stringify({"id": dockey, "content": edit_doc});
        var token = $("meta[name='_csrf']").attr("content");
        var urls = [];
        $.ajax({
            url: '/admin/edit_doc',
            headers: {"X-CSRF-TOKEN": token},
            data: sendData,
            method: 'POST',
            dataType: 'html',
            contentType: 'application/json',
            success: function (res) {
                set_Guide_update(selectedText,'update');
                issuc = true;
                // refresh page
            }, error: function (error) {
                console.log(error);
            }
        });

        //IMAGE URL
        //Get before URL
        var beforeURL = new Set();
        var afterURL = new Set();
        //Get after URL
        //Extract insertUrl & deleteUrl
        //Insert URL to DB
        //Delete URL from DB

        urls = UrlParse(sendData);
        beforeURL;
        afterURL;

        //Tag Select2
        afterTags = new Set();
        $.each($('select.select2-tagging option:selected'), function (key, val) {
            afterTags.add(val.text);
        });

        var insertTags = [];
        var deleteTags = [];

        insertTags = substract(Array.from(afterTags), Array.from(beforeTags));
        deleteTags = substract(Array.from(beforeTags), Array.from(afterTags));

        var issuc = false;
        $.ajax({
            'url': '/admin/updateTags',
            'contentType': 'application/json',
            'async': false,
            'data': JSON.stringify({'insert': insertTags, 'delete': deleteTags, 'doc_key': selectedData}),
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
function get_Guide_update(title) {
    console.log(title);
    $.ajax({
        url: '/admin/get_update?title=' + title,
        method: 'GET',
        success: function (res) {
            //update_KEY: 1, admin_ID: "jun.3671", document_TITLE: "ex1", update_TIME: "2020-01-24T19:28:25.000+0000", update_TYPE_CUD: "update
            var ttemp = new Array();
            var ttext = " ";
            for(var k=0;k<res.length;k++) {
                ttemp = Object.values(res[k]);
                for (var i = 1; i < ttemp.length; i++) {
                    ttext += ttemp[i];
                    ttext += " - ";
                }
                $('#update').val(ttext); // async 로 작동함
            }

        }, error: function (error) {
            console.log(error);
        }
    });
}


//set guide_update when save button clicked
function set_Guide_update(title,type) {
    var sendData = JSON.stringify({"admin": $('#admin_name').val(),"title": title, "CRUD": type});
    console.log(sendData);
    var token = $("meta[name='_csrf']").attr("content");
    $.ajax({
        url: '/admin/set_update',
        headers: {"X-CSRF-TOKEN": token},
        data: sendData,
        method: 'POST',
        dataType:'html',
        contentType:'application/json',
        success: function (res) {
            console.log("update");
        }, error: function (error) {
            console.log(error);
        }
    });
}

//get guide_tag when node is selected
/*
function get_Guide_tag(title) {
    console.log(title);
    $.ajax({
        url: '/admin/get_tag?doc_key=' + title,
        method: 'GET',
        success: function (res) {
            //update_KEY: 1, admin_ID: "jun.3671", document_TITLE: "ex1", update_TIME: "2020-01-24T19:28:25.000+0000", update_TYPE_CUD: "update
            var ttemp = new Array(); var ttext = " ";
            ttemp = Object.values(res);
            for(var i=1;i<ttemp.length;i++) {
                ttext += ttemp[i];
                ttext += " - ";
            }
            $('#update').val(ttext);
            console.log(res);
        }, error: function (error) {
            console.log(error);
        }
    });
}*/
function init_select_tagging(){
    var ret = [];
    $.ajax({
        'async':false,
        'url':'admin/getTags',
        'data':{'doc_key':selectedData},
        'success':function(data){
            $.each(data, function(key, val){
                ret.push({"id" : key, "text":val.tag, "selected":true});
            });
        }
    });

    $('select.select2-tagging').select2(
        {
            'ajax':{
                'url':'admin/suggestTags',
                'data':function(params) {
                    return {'tag':params.term};
                },
                'processResults':function(data) {
                    data = $.map(data, function (obj) {
                        obj.id = obj.id || obj.tag; // replace pk with your identifier
                        obj.text = obj.text || obj.tag; // replace pk with your identifier
                        return obj;
                    });
                    return {results : data};
                }
            },
            'tags':true,
            'allowClear':true,
            'disabled':true,
            'tokenSeparators':[',',' '], //태그 구분자 추가
            'createTag':function(params){ //태그 공백 제거
                var term = $.trim(params.term);
                if(term == '') {
                    return null;
                }
                else {
                    return {'id':term, 'text':term,'newTag':true};
                }
            }
        }
    );

    var options = "";
    $.each(ret, function(key, val){
        options += "<option selected='selected' value='" + val.text + "'>" + val.text +"</option>"
        beforeTags.add(val.text);
    });
    $('select.select2-tagging').html(options);
}

function substract(a,b) { return $(a).not(b).get(); }

function UrlParse(text) {
    var m,
        urls = [],
        str = text,
        rex = /[sS][rR][cC]\s*=\s*(?:'|")([^("|')]*)(?:'|")/g;

    while ( m = rex.exec( str ) ) {
        urls.push( m[1] );
    }
    return urls;
}