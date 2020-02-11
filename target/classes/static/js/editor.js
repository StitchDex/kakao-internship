const domain = 'http://guide.at.kakaocorp.com/admin/imageupload';

class MyUploadAdapter {
    constructor(loader) {
        this.loader = loader;
    }
    upload() {
        return this.loader.file
            .then(file => new Promise((resolve, reject) => {
                this._initRequest();
                this._initListeners(resolve, reject, file);
                this._sendRequest(file);
            }));
    }
    abort() {
        if (this.xhr) {
            this.xhr.abort();
        }
    }
    _initRequest() {
        const xhr = this.xhr = new XMLHttpRequest();
        xhr.open('POST', 'http://guide.at.kakaocorp.com/admin/imageupload', true);
        //xhr.open( 'POST', 'http://localhost:8080/admin/imageupload', true );
        xhr.responseType = 'json';
    }
    _initListeners(resolve, reject, file) {
        const xhr = this.xhr;
        const loader = this.loader;
        const genericErrorText = `Couldn't upload file: ${file.name}.`;
        xhr.addEventListener('error', () => reject(genericErrorText));
        xhr.addEventListener('abort', () => reject());
        xhr.addEventListener('load', () => {
            const response = xhr.response;
            if (!response || response.error) {
                return reject(response && response.error ? response.error.message : genericErrorText);
            }
            resolve({
                default: response.url
            });
        });
        if (xhr.upload) {
            xhr.upload.addEventListener('progress', evt => {
                if (evt.lengthComputable) {
                    loader.uploadTotal = evt.total;
                    loader.uploaded = evt.loaded;
                }
            });
        }
    }
    _sendRequest(file) {

        const data = new FormData();
        var token = $("meta[name='_csrf']").attr("content");
        this.xhr.setRequestHeader("X-CSRF-TOKEN", token);
        data.append('type', "image");
        data.append('upload', file);
        this.xhr.send(data);
    }
}

function MyCustomUploadAdapterPlugin(editor) {
    editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {

        return new MyUploadAdapter(loader);
    };
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