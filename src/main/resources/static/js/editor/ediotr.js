class MyUploadAdapter {
    constructor( loader ) {
        this.loader = loader;
    }
    upload() {
        return this.loader.file
            .then( file => new Promise( ( resolve, reject ) => {
                this._initRequest();
                this._initListeners( resolve, reject, file );
                this._sendRequest( file );
            } ) );
    }
    abort() {
        if ( this.xhr ) {
            this.xhr.abort();
        }
    }
    _initRequest() {
        const xhr = this.xhr = new XMLHttpRequest();
        //여기서는 POST 요청과 json으로 응답을 받지만 어떤 포맷으로 하든 너의 선택이다.
        xhr.open( 'POST', 'http://localhost:8080/guide/imageupload', true );
        console.log(this.xhr);
        xhr.responseType = 'json';
    }
    //XHR 리스너 초기화 하기
    _initListeners( resolve, reject, file ) {
        const xhr = this.xhr;
        const loader = this.loader;
        const genericErrorText = `Couldn't upload file: ${ file.name }.`;
        xhr.addEventListener( 'error', () => reject( genericErrorText ) );
        xhr.addEventListener( 'abort', () => reject() );
        xhr.addEventListener( 'load', () => {
            const response = xhr.response;
            // 이 예제에서는 XHR서버에서의 response 객체가 error와 함께 올 수 있다고 가정한다. 이 에러는
            // 메세지를 가지며 이 메세지는 업로드 프로미스의 매개변수로 넘어갈 수 있다.

            if ( !response || response.error ) {
                return reject( response && response.error ? response.error.message : genericErrorText );
            }

            // 만약 업로드가 성공했다면, 업로드 프로미스를 적어도 default URL을 담은 객체와 함께 resolve하라.
            // 이 URL은 서버에 업로드된 이미지를 가리키며, 컨텐츠에 이미지를 표시하기 위해 사용된다.
            resolve( {
                default: response.url
            } );
        } );

        // 파일로더는 uploadTotal과 upload properties라는 속성 두개를 갖는다.
        // 이 두개의 속성으로 에디터에서 업로드 진행상황을 표시 할 수 있다.
        if ( xhr.upload ) {
            xhr.upload.addEventListener( 'progress', evt => {
                if ( evt.lengthComputable ) {
                    loader.uploadTotal = evt.total;
                    loader.uploaded = evt.loaded;
                }
            } );
        }
    }
//데이터를 준비하고 서버에 전송한다.
    _sendRequest( file ) {
        // 폼 데이터 준비
        const data = new FormData();
        data.append( 'upload', file );

        // 여기가 인증이나 CSRF 방어와 같은 방어 로직을 작성하기 좋은 곳이다.
        // 예를들어, XHR.setREquestHeader()를 사용해 요청 헤더에 CSRF 토큰을 넣을 수 있다.

        this.xhr.send( data );
    }
}


function MyCustomUploadAdapterPlugin( editor ) {
    editor.plugins.get( 'FileRepository' ).createUploadAdapter = ( loader ) => {
        // Configure the URL to the upload script in your back-end here!
        // 결국엔 내가 구현해 주어야 할 것은,
        // FileRepository가 어떤 업로드 어댑터를 사용하게 하느냐만 설정해주면 된다.
        // 나머지 이미지 업로드 플러그인, 파일 로더, FileRepository등등은 이미 만들어져 있다.
        return new MyUploadAdapter( loader );
    };
}

ClassicEditor
    .create( document.querySelector( '#editor' ), {
            //extraPlugins:[MyCustomUploadAdapterPlugin],
            toolbar: ["bold", "heading", "imageTextAlternative", "imageStyle:full", "imageStyle:side", "imageUpload", "indent", "outdent", "italic", "link", "numberedList", "bulletedList", "insertTable", "tableColumn", "tableRow", "mergeTableCells", "alignment:left", "alignment:right", "alignment:center", "alignment:justify", "alignment", "code", "fontSize", "underline", "undo", "redo"],

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
        //console.log( Array.from( editor.ui.componentFactory.names() ) );
        console.log(editor);
    })
    .catch( error => {
        //console.error( error );
    }
    );

//for read doc
