function adjustModalMaxHeightAndPosition() {
    $('.modal').each(function() {
        if ($(this).hasClass('in') == false) {
            $(this).show(); /* Need this to get modal dimensions */
        }
        ;
        var contentHeight = $(window).height() - 60;
        var headerHeight = $(this).find('.modal-header').outerHeight() || 2;
        var footerHeight = $(this).find('.modal-footer').outerHeight() || 2;

        $(this).find('.modal-content').css({
            'max-height' : function() {
                return contentHeight;
            }
        });

        $(this).find('.modal-body').css({
            'max-height' : function() {
                return (contentHeight - (headerHeight + footerHeight));
            }
        });

        $(this).find('.modal-dialog').addClass('modal-dialog-center').css({

        }).css({

            'margin-top' : function() {
                return -($(this).outerHeight() / 2);
            },
            'margin-left' : function() {
                return -($(this).outerWidth() / 2);
            },

        });
        if ($(this).hasClass('in') == false) {
            $(this).hide(); /* Hide modal */
        }
        ;
    });
};
if ($(window).height() >= 320) {
    $(window).resize(adjustModalMaxHeightAndPosition).trigger("resize");
}