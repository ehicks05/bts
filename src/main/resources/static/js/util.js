/* Wires up the following:
   1. A button that opens the dialog
   2. The top-right x that closes the dialog
   3. A 'close' button that closes the dialog
 */
function initDialog (prefix)
{
    $(function () {
        var dialog = $('#' + prefix + 'Dialog');
        var openDialogButton = $('#' + prefix + 'Button');
        var modalBackground = dialog.find('.modal-background');

        openDialogButton.on('click', toggleDialog);
        dialog.find('.close').on('click', toggleDialog);
        dialog.find('button[class="delete"]').on('click', toggleDialog);
        modalBackground.on('click', toggleDialog);
                    
        function toggleDialog() {
            dialog.toggleClass('is-active');
        }

        $(document).keyup(function (e)
        {
            if (e.keyCode === 27 && dialog.hasClass('is-active')) { // escape key maps to keycode `27`
                dialog.toggleClass('is-active');
            }
        });
    });
}