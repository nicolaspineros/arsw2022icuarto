
app = (function (){
    var _author;
    var _blueprints = [];

    var _setTable = function(blueprints) {
        var lista = blueprints.map(function(blueprint){
            return{
                name: blueprint.name, points: blueprint.points.length
            }})
        $("#tb-body").empty();                
        lista.map(bp => {
            $("#tb-body").append($('<tr><td id="nameAuthor">'+bp.name+'</td><td id="points">'+bp.points+'</td><td><button class="bt-bp">Open</button></td></tr>').on("click","button",() => drawBlueprint(_author,bp.name)));
        });

        var total = lista.reduce((vtotal, {points})=>vtotal.points + points);
        $("#puntosText").text("Total user points: "+total);
    };

    var generateListBp = function(author) {
        _author = author;
        $("#sb-author").text(author+"'s Blueprints:");
        apimock.getBlueprintsByAuthor(author,_setTable);        
    };

    var _drawBP = function(blueprint){
        console.log(blueprint);
        $("#current").text("Current Blueprint: "+blueprint.name);
        var points = blueprint.points;
        var canvas = document.getElementById("canvasBp");
        var ctx = canvas.getContext("2d");

        canvas.wigth = canvas.wigth;        

        if (canvas.getContext) {
            ctx.beginPath();
            ctx.moveTo(points[0].x, points[0].y);   
            for(var i=1; i<points.length;i++){
                console.log(points[i].x,points[i].y)
                ctx.lineTo(points[i].x,points[i].y);
                ctx.clearRect(0,0,canvas.wigth,canvas.height);
            }
            ctx.closePath();
            ctx.stroke();
        }
    };

    var drawBlueprint = function(author,bpName){
        apimock.getBlueprintsByNameAndAuthor(author,bpName,_drawBP);
    };

    
    return{        
        generateListBp: generateListBp,
        drawBlueprint: drawBlueprint
    }
})();