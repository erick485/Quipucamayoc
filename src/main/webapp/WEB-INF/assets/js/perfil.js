
$(function(){
    $("#opciones").popover({
        placement: 'bottom',
        html: 'true',
        content : '<div style="width: 108px;"><a href="#"><span class="glyphicon glyphicon-globe"></span> Ayuda</a></div>'+
            '<div id="herramienta"><a href="#"><span class="glyphicon glyphicon-cog"></span> Herramientas</a></div>'+
            '<div id="close"><a href="#"><span class="glyphicon glyphicon-road"></span> Cerrar Sesion</a></div>'
    });
});
