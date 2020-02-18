//메뉴 클릭
$(function () {

    hiddenNum = 0;
    depth2Dir = 0;
    documentKey = $('#selected').val();
    isEditing = false;
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
                            hidden[hiddenNum++] = data[i].id;
                            data[i].text = "(숨김) "+data[i].text;
                        }
                    }
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
        },'sort': function (a, b) {
            var a1 = this.get_node(a);
            var b1 = this.get_node(b);
            if (a1.parent === b1.parent) {
                return (a1.original.orders > b1.original.orders) ? 1 : -1;
            }
        },

        'plugins': ["types", "state", "wholerow",'sort']
    }).delegate("a", "dblclick", function () {
        $(this).parents(".jstree:eq(0)").jstree("toggle_node", this);
    }).on('ready.jstree', function () {
        beforeTreeOpen($(this).jstree('close_all'), openRoot());
        if (documentKey != null) {
            selectOpen();
        }
    });
    $('#jstree').jstree('clear_state');

    if (documentKey != null) {
        $.ajax({
            'url': '/guide/menu',
            'data': {'doc_key': documentKey},
            'async': false,
            'success': function (res) {
                if ((window.location.pathname.startsWith("/guide") && res.state == 0) || res === "") { //HIDDEN GUIDE
                    location.href = "/error";
                }
                var title = res.text;
                $('#guide-title').text(title);
                if (guideEditor != null) { // change doc
                    guideEditor.destroy();
                    makeGuideEditor(res.content);
                } else { // document_ready
                    makeGuideEditor(res.content);
                }
                initSelectTagging();
                getGuideUpdate(documentKey);
            },
            'error': function (error) {
                console.log(error);
                location.href = "/error";
            },
        });
    }
});

$('#jstree').on('select_node.jstree', function (e, data) {

    selectedData = data.node.id;
    selectedText = data.node.text;

    if (selectedData.startsWith("DIR")) {//click dir_node
        data.node.state.opened ? $(this).jstree('close_node', selectedData)
            : $(this).jstree('open_node', selectedData);
    }
    else {
        loadDoc();//click page_node
    }
});

//선택한 노드의 부모까지 열어준다
function selectOpen() {
    let cur = $('#jstree').jstree('get_node', "DOC" + documentKey);
    while (cur.parent != "#") {
        $('#jstree').jstree('open_node', cur.parent);
        let curParent = cur.parent;
        cur = $('#jstree').jstree('get_node', curParent);
    }
}

//숨김 상태의 노드를 숨긴다
function beforeTreeOpen() {
    if(window.location.pathname.startsWith("/guide")){
        for (var i = 0; i < hidden.length; i++) {
            $("#jstree").jstree(true).hide_node(hidden[i]);
        }
    }

}

//Depth1 노드까지 열어준다
function openRoot() {
    let cur = $('#jstree').jstree('get_node','#');
    let childArray = cur.children;
    $.each(childArray,function (index,item) {
        $('#jstree').jstree('open_node',item);
    })
}