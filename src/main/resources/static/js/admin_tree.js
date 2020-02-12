var hiddenNum;
var hidden = new Array();
var token = $("meta[name='_csrf']").attr("content");
let curPosition;
$(function () {
    hiddenNum = 0;
    $('#edit_tree').jstree({
        'core': {
            'multiple': false,
            'check_callback': true,
            'data': {
                'url': '/guide/tree',
                'type': "GET",
                'dataType': 'json',
                'data': function (node) {
                },
                'success': function (data) {
                    for (i = 0; i < data.length; i++) {
                        var temp = (data[i].state);
                        if (!temp) {
                            hidden[hiddenNum++] = data[i].id;
                        }
                    }
                    console.log(data);
                },
            },
        },
        'themes': {
            'variant': 'large',
            'responsive': true
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
        "cookies": {
            "save_selected": false,
            'save_opened': false,
            'auto_save': false
        },
        'contextmenu': {
            "items": function ($node) {
                var tree = $("#edit_tree").jstree(true);
                return {
                    "Create": {
                        "separator_before": false,
                        "separator_after": true,
                        "label": "생성",
                        "action": false,
                        "submenu": {
                            "File": {
                                "seperator_before": false,
                                "seperator_after": false,
                                "label": "File",
                                action: function (obj) {
                                    $node = tree.create_node($node, {
                                        text: 'NewDOC',
                                        type: 'DOC',
                                        icon: 'far fa-file',
                                        state: {
                                            'disabled': false
                                        }
                                    });
                                    createNode(getCreateJson(tree, $node));
                                }
                            },
                            "Folder": {
                                "seperator_before": false,
                                "seperator_after": false,
                                "label": "Folder",
                                action: function (obj) {
                                    $node = tree.create_node($node, {
                                        text: 'NewDIR',
                                        type: 'DIR',
                                        icon: 'far fa-folder',
                                        state: {
                                            'disabled': false
                                        }
                                    });
                                    createNode(getCreateJson(tree, $node));
                                }
                            }
                        }
                    },
                    "Rename": {
                        "label": "이름수정",
                        "action": function (data) {
                            tree.edit($node, null, function (node, status) {
                                node.text = $.trim(node.text);
                                var textLength = node.text.length;
                                if (textLength > 40 || textLength <= 0) {
                                    alert("공백없이 1자 이상 40자 이하의 이름만 가능합니다.");
                                    location.reload();
                                    return;
                                } else {
                                    var jsonData = {
                                        'id': node.id,
                                        'parent': node.parent,
                                        'text': node.text,
                                        'type': node.type,
                                        'order': node.original.orders,
                                        'state': !node.state.disabled
                                    };
                                    updateNode(jsonData, node.type);
                                }
                            });
                        }
                    },
                    "Delete": {
                        "label": "삭제",
                        "action": function (data) {
                            var delnode = $node.id;
                            var title = $node.text;
                            var del = confirm('삭제?');
                            if (del) {
                                if ($node.type != "DIR") {
                                    tree.delete_node($node);
                                    deleteNode(delnode, title, "DOC");
                                } else {
                                    if ($node.children.length > 0)
                                        alert('하위 파일이 존재합니다');
                                    else {
                                        tree.delete_node($node);
                                        deleteNode(delnode, title, "DIR");
                                    }
                                }

                            }
                        }
                    },
                    "Hidden": {
                        "label": "숨김",
                        "action": function (data) {
                            var cur = $node.state.disabled;
                            var jsonData = {
                                'id': $node.id,
                                'parent': $node.parent,
                                'text': $node.text,
                                'type': $node.type,
                                'order': $node.original.orders,
                                'state': $node.state.disabled
                            };
                            if ($node.children.length > 0) {
                                alert('하위 파일이 존재합니다');
                            } else if ($node.type != "DIR") {
                                if (!cur)
                                    tree.disable_node($node);
                                else {
                                    tree.enable_node($node);
                                }
                                updateNode(jsonData, $node.type);
                            } else {
                                if (!cur)
                                    tree.disable_node($node);
                                else {
                                    tree.enable_node($node);
                                }
                                updateNode(jsonData, $node.type);
                            }

                        }
                    }
                }
            }

        }, 'sort': function (a, b) {
            var a1 = this.get_node(a);
            var b1 = this.get_node(b);
            if (a1.parent === b1.parent) {
                return (a1.original.orders > b1.original.orders) ? 1 : -1;
            }
        },
        'plugins': ["types", "dnd", "contextmenu", "cookies", 'sort']
    }).on('ready.jstree', function () {
        makeDisable();
        $(this).jstree('open_all')
    })
        .bind("move_node.jstree", function (e, data) {
            console.log(data);
            var temp = {
                'id': data.node.id,
                'parent': data.node.parent,
                'text': data.node.text,
                'type': data.node.type,
                'order': data.position,
                'state': !data.node.state.disabled
            };
            updateNode(temp);
        })
        .on("select_node.jstree", function (e, data) {
            curPosition = data.node.children.length;
            console.log(data.node, curPosition);
        });

});

$('#edit_tree').on('select_node.jstree', function (e, data) {

    selectedData = data.node.id;
    selectedText = data.node.text;

    if (selectedData.startsWith("DIR")) {//click dir_node
        data.node.state.opened ? $(this).jstree('close_node', selectedData)
            : $(this).jstree('open_node', selectedData);
    }
});

//hidden -> disable
function makeDisable() {
    for (var i = 0; i < hidden.length; i++) {
        var node = $('#edit_tree').jstree(true).get_node(hidden[i]);
        $("#edit_tree").jstree(true).disable_node(node);

    }
}

function getCreateJson(tree, $node) {
    var temp = tree.get_node($node);
    var jsonData = {
        'id': temp.id,
        'parent': temp.parent,
        'text': temp.text,
        'type': temp.type,
        'order': curPosition,
        'state': !temp.state.disabled
    };
    return jsonData;
}

function createNode(sendData) {
    var title = sendData.text;
    sendData = JSON.stringify(sendData);
    $.ajax({
        url: '/admin/admin_tree/create',
        headers: {"X-CSRF-TOKEN": token},
        data: sendData,
        method: 'POST',
        dataType: 'html',
        contentType: 'application/json',
        success: function (res) {
            if (res > 0) {
                setGuideUpdate(title, res, 'create');
                alert("파일 생성 성공");
                opener.document.location.reload();
                location.reload();
            } else {
                setGuideUpdate(title, null
                    , 'create');
                alert("폴더 생성 성공");
                opener.document.location.reload();
                location.reload();
            }
        }, error: function (error) {
            alert("트리 생성 오류");
            console.log(error);
        }
    });
}

function updateNode(sendData) {
    var title = sendData.text;
    sendData = JSON.stringify(sendData);
    $.ajax({
        url: '/admin/admin_tree/update',
        headers: {"X-CSRF-TOKEN": token},
        data: sendData,
        method: 'POST',
        dataType: 'html',
        contentType: 'application/json',
        success: function (res) {
            var temp = JSON.parse(sendData);
            setGuideUpdate(title, temp.id, 'change');
            alert("트리 업데이트 성공");
            opener.document.location.reload();
            location.reload();
        }, error: function (error) {
            alert("트리 업데이트 오류");
            console.log(error);
        }
    });
}

function deleteNode(sendData, title, what) {
    sendData = JSON.stringify({'id': sendData, 'type': what});
    $.ajax({
        url: '/admin/admin_tree/delete',
        headers: {"X-CSRF-TOKEN": token},
        data: sendData,
        method: 'POST',
        dataType: 'html',
        contentType: 'application/json',
        success: function (res) {
            var temp = JSON.parse(sendData);
            setGuideUpdate(title, temp.id, 'delete');
            alert("트리 삭제 성공");
            opener.document.location.reload();
            location.reload();
        }, error: function (error) {
            alert("트리 삭제 오류");
            console.log(error);
        }
    });
}

function createRootJson() {
    var temp = $("#edit_tree").jstree("get_node", '#');
    curPosition = temp.children.length;
    var json_data = {
        'parent': '#',
        'text': 'New Root',
        'type': 'DIR',
        'state': false,
        'order': curPosition
    };
    createNode(json_data);
}
