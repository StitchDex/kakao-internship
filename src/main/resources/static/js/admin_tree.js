var hiddenNum;
var hidden = new Array();
var token = $("meta[name='_csrf']").attr("content");
let curPosition;
let mainDocument;
$(function () {
    hiddenNum = 0;
    let jsonArray = new Array();
    mainDocument = $('#mainDocument').val();
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
                                action: function (data) {
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
                                action: function (data) {
                                    console.log(data);
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
                        "action": function () {
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
                                        'orders': node.original.orders,
                                        'state': node.original.state + 0
                                    };
                                    jsonArray.push(jsonData);
                                    updateNode(jsonArray);
                                }
                            });
                        }
                    },
                    "Delete": {
                        "label": "삭제",
                        "action": function () {
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
                        "action": function () {
                            var cur = $node.state.disabled;
                            var jsonData = {
                                'id': $node.id,
                                'parent': $node.parent,
                                'text': $node.text,
                                'type': $node.type,
                                'orders': $node.original.orders,
                                'state': cur + 0
                            };
                            jsonArray.push(jsonData);
                            if ($node.children.length > 0) {
                                alert('하위 파일이 존재합니다');
                            } else if ($node.original.state == 2) {
                                alert("메인 문서는 숨길수 없습니다.")
                            } else if ($node.type != "DIR") {
                                if (!cur)
                                    tree.disable_node($node);
                                else {
                                    tree.enable_node($node);
                                }
                                updateNode(jsonArray);
                            } else {
                                if (!cur)
                                    tree.disable_node($node);
                                else {
                                    tree.enable_node($node);
                                }
                                updateNode(jsonArray);
                            }

                        }
                    },
                    "Main": {
                        "label": "메인 설정",
                        "action": function () {
                            if ($node.type == "DIR") {
                                alert("문서만 선택 가능합니다.")
                            } else if ($node.original.state == 2) {
                                alert("이미 선택한 문서입니다.")
                            } else {
                                var jsonData = {
                                    'id': mainDocument.id,
                                    'parent': mainDocument.parent,
                                    'text': mainDocument.text,
                                    'type': mainDocument.type,
                                    'orders': mainDocument.original.orders,
                                    'state': 1
                                };
                                var jsonData1 = {
                                    'id': $node.id,
                                    'parent': $node.parent,
                                    'text': $node.text,
                                    'type': $node.type,
                                    'orders': $node.original.orders,
                                    'state': 2,
                                };
                                jsonArray.push(jsonData);
                                jsonArray.push(jsonData1);
                                updateNode(jsonArray);
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
        $(this).jstree('open_all');
        mainDocument = $(this).jstree('get_node', "DOC" + mainDocument);
        $('.mainDocumentText').text(mainDocument.text);
    }).bind("move_node.jstree", function (e, data) {

        if (data.old_parent === data.parent) {
            var nodeParent = $("#edit_tree").jstree('get_node', data.parent); //부모
            var temp = $("#edit_tree").jstree('get_node', nodeParent.children[data.position]);
            console.log(temp);
            var jsonData = {
                'id': temp.id,
                'parent': temp.parent,
                'text': temp.text,
                'type': temp.type,
                'orders': data.old_position,
                'state': !temp.state.disabled + 0
            };
            jsonArray.push(jsonData);
            temp = $("#edit_tree").jstree('get_node', nodeParent.children[data.old_position]);
            console.log(temp);
            jsonData = {
                'id': temp.id,
                'parent': temp.parent,
                'text': temp.text,
                'type': temp.type,
                'orders': data.position,
                'state': !temp.state.disabled + 0
            };
            jsonArray.push(jsonData);
        }
        else{ //다른 디렉터리 이동
            nodeParent = $("#edit_tree").jstree('get_node', data.old_parent); //이전 부모
            var nodeParent1 = $("#edit_tree").jstree('get_node', data.parent); // 바뀐 부모
            console.log(nodeParent);
            let len = nodeParent.children.length;
            let i = 0;
            while (len > 0) {
                var temp = $("#edit_tree").jstree('get_node', nodeParent.children[i]);
                console.log(temp);
                if (temp.text != data.text) {
                    var jsonData = {
                        'id': temp.id,
                        'parent': temp.parent,
                        'text': temp.text,
                        'type': temp.type,
                        'orders': i,
                        'state': !temp.state.disabled + 0
                    };
                    jsonArray.push(jsonData);
                    i++;
                }
                len--;
            }
            len = nodeParent1.children.length;
            i = 0;
            while (len > 0) {
                var temp = $("#edit_tree").jstree('get_node', nodeParent1.children[i]);
                console.log(temp);
                if (i == data.position) {
                    var jsonData = {
                        'id': data.node.id,
                        'parent': data.node.parent,
                        'text': data.node.text,
                        'type': data.node.type,
                        'orders': data.position,
                        'state': !data.node.state.disabled + 0
                    };
                    jsonArray.push(jsonData);
                    var jsonData = {
                        'id': temp.id,
                        'parent': temp.parent,
                        'text': temp.text,
                        'type': temp.type,
                        'orders': i+1,
                        'state': !temp.state.disabled + 0
                    };
                    jsonArray.push(jsonData);
                    i++;
                }
                else{
                    var jsonData = {
                        'id': temp.id,
                        'parent': temp.parent,
                        'text': temp.text,
                        'type': temp.type,
                        'orders': i,
                        'state': !temp.state.disabled + 0
                    };
                    jsonArray.push(jsonData);
                    i++;
                }
                len--;
            }
        }
        updateNode(jsonArray);
    }).on("select_node.jstree", function (e, data) {
        curPosition = data.node.children.length;
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
    try {
        var jsonData = {
            'id': temp.id,
            'parent': temp.parent,
            'text': temp.text,
            'type': temp.type,
            'orders': curPosition,
            'state': !temp.state.disabled + 0
        };
        return jsonData;
    } catch {
        alert('생성은 폴더 클릭시에만 가능합니다.');
    }
}

function createNode(sendData) {
    var title = sendData.text;
    sendData = JSON.stringify(sendData);
    $.ajax({
        url: '/admin/admin_tree/create',
        headers: {"X-CSRF-TOKEN": token},
        data: sendData,
        method: 'POST',
        async: 'false',
        dataType: 'html',
        contentType: 'application/json',
        success: function (res) {
            if (res > 0) {
                setGuideUpdate(title, res, 'create');
                alert(title + "생성 성공");
                opener.document.location.reload();
                location.reload();
            }
        }, error: function (error) {
            alert("트리 생성 오류");
            console.log(error);
            location.reload();
        }
    });
}

function updateNode(sendData) {
    var title = sendData[0].text;
    sendData = JSON.stringify(sendData);
    $.ajax({
        url: '/admin/admin_tree/update',
        headers: {"X-CSRF-TOKEN": token},
        data: sendData,
        async: false,
        method: 'POST',
        dataType: 'html',
        contentType: 'application/json',
        success: function () {
            var temp = JSON.parse(sendData);
            setGuideUpdate(title, temp.id, 'change');
            alert(title + "업데이트 성공");
            opener.document.location.reload();
            location.reload();
        }, error: function (error) {
            alert("트리 업데이트 오류");
            console.log(error);
            location.reload();
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
        async: 'false',
        dataType: 'html',
        contentType: 'application/json',
        success: function () {
            var temp = JSON.parse(sendData);
            setGuideUpdate(title, temp.id, 'delete');
            alert(title + "삭제 성공");
            opener.document.location.reload();
            location.reload();
        }, error: function (error) {
            alert("트리 삭제 오류");
            console.log(error);
            location.reload();
        }
    });
}

function createRootJson() {
    var temp = $("#edit_tree").jstree("get_node", '#');
    curPosition = temp.children.length;
    var jsonData = {
        'parent': '#',
        'text': 'New Root',
        'type': 'DIR',
        'state': 0,
        'orders': curPosition
    };
    createNode(jsonData);
}
