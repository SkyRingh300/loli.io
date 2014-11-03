String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

function viewFile(file, img) {
    // 通过file.size可以取得图片大小
    var reader = new FileReader();
    reader.onload = function(evt) {
        $(img).attr("src", evt.target.result);
    }
    reader.readAsDataURL(file);
}

$(function() {

    var ul = $('#list-upload ul');

    $('.img-select-btn').click(function() {
        // Simulate a click on the file input button
        // to show the file browser dialog
        $(this).parent().find('input').click();
    });

    // Initialize the jQuery File Upload plugin
    $('#list-upload')
        .fileupload(
            {
                // This element will accept file drag/drop uploading
                dropZone : $('#drop'),

                // This function is called when a file is added to the
                // queue;
                // either via the browse button, or via drag/drop:
                add : function(e, data) {
                    if (data.files[0].type.indexOf("image") < 0) {
                        alert("请选择图片文件！");
                        return;
                    }
                    var tpl = $('<li class="working"><!--<input type="text" value="0" data-width="48" data-height="48"'
                        + ' data-fgColor="#0788a5" data-readOnly="1" data-bgColor="#3e4043" />--><img class="thumb"><p class="name"></p><!--<span></span>--><label class="path">图片上传中...</label>');

                    // Append the file name and file size
                    if (!data.files[0].name) {
                        data.files[0].name = '剪贴板.png';
                    }
                    tpl.find('p').text(data.files[0].name + ' (' + formatFileSize(data.files[0].size) + ')');

                    // Add the HTML to the UL element
                    data.context = tpl.appendTo(ul);

                    // Initialize the knob plugin
                    tpl.find('input').knob();

                    var img = tpl.find("img").eq(0);

                    viewFile(data.files[0], img);

                    // Listen for clicks on the cancel icon
                    tpl.find('span').click(function() {

                        if (tpl.hasClass('working')) {
                            jqXHR.abort();
                        }

                        tpl.fadeOut(function() {
                            tpl.remove();
                        });

                    });

                    // Automatically upload the file once it is added to
                    // the queue
                    var jqXHR = data.submit();
                },
                progress : function(e, data) {
                    $("#drop").hide();
                    $("#upload-continue").show();
                    $("#upload-batch-url").show();
                    // Calculate the completion percentage of the upload
                    var progress = parseInt(data.loaded / data.total * 100, 10);

                    // Update the hidden input field and trigger a
                    // change
                    // so that the jQuery knob plugin knows to update
                    // the dial

                    data.context.find('label').eq(0).html("图片上传中:" + progress + "%");
                    if (progress == 100) {
                        data.context.find('label').eq(0).html("上传成功, 正在生成链接...");
                    }
                },

                fail : function(e, data) {
                    // Something has gone wrong!
                    data.context.addClass('error');
                    data.context.find('label').eq(0).html("图片上传失败");

                },
                done : function(e, data) {
                    var filename = data.result.redirectCode;
                    var prefix = $("#redirectPath").val();
                    if (prefix) {
                        filename = prefix + filename;
                    } else {
                        filename = data.result.redirectCode;
                    }
                    if (filename) {
                        data.context.find('label').eq(0).html("图片上传失败");
                    }
                    content = "<a target='_blank' href='";
                    content += filename;
                    content += "'>";
                    content += filename;
                    content += "</a>";
                    data.context.find('label').eq(0).html(content);

                    setTimeout(function() {
                        reloadImages(getGid(), 1);
                        loadPage(getGid());
                    }, 3000);
                }

            });

    // Prevent the default action when a file is dropped on the window
    $(document).on('drop dragover', function(e) {
        e.preventDefault();
    });

    // Helper function that formats the file sizes
    function formatFileSize(bytes) {
        if (typeof bytes !== 'number') {
            return '';
        }

        if (bytes >= 1000000000) {
            return (bytes / 1000000000).toFixed(2) + ' GB';
        }

        if (bytes >= 1000000) {
            return (bytes / 1000000).toFixed(2) + ' MB';
        }

        return (bytes / 1000).toFixed(2) + ' KB';
    }

});