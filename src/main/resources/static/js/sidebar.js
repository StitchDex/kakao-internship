//메뉴 클릭
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
function select_open() {
    let cur = $('#jstree').jstree('get_node', "DOC" + documentKey);
    while (cur.parent != "#") {
        $('#jstree').jstree('open_node', cur.parent);
        let cur_p = cur.parent;
        cur = $('#jstree').jstree('get_node', cur_p);
    }
}

//숨김 상태의 노드를 숨긴다
function before_tree_open() {
    for (var i = 0; i < hidden.length; i++) {
        $("#jstree").jstree(true).hide_node(hidden[i]);
    }
}

//Depth1 노드까지 열어준다
function open_root() {
    let cur = $('#jstree').jstree('get_node','#');
    let childArray = cur.children;
    $.each(childArray,function (index,item) {
        $('#jstree').jstree('open_node',item);
        console.log(item);
    })
}