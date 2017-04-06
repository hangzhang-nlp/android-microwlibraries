var MM = Microsoft.Maps;

var BingMapsAndroid = new function(){
	var _mapId,
		_map,
		_bingMapKey,
		_layerManager,
		_infobox;

	this.AddLayer = function(data, view){
		if(data != null){
			var l = _layerManager.GetLayer(data.LayerName);
			if(l == null)
			{
				_layerManager.AddLayer(data.LayerName);
				l = _layerManager.GetLayer(data.LayerName);
			}

			l.AddEntities(data.Entities);
			
			if(view != null){
				_map.setView({ bounds : view });
			}
		}
	};
	
	this.ClearLayer = function(layerName){
		if(layerName != null && layerName != '')
		{
			var l = _layerManager.GetLayer(layerName);
			if(l != null)
			{
				l.ClearLayer();
			}
		}
		else{
			_layerManager.DeleteAllLayers();
			_map.entities.clear();
		}
	};
	
	this.HideLayer = function(layerName){
		if(layerName != null && layerName != '')
		{
			var l = _layerManager.GetLayer(layerName);
			if(l != null)
			{
				l.HideLayer ();
			}
		}
	};
	
	this.ShowLayer = function(layerName){
		if(layerName != null && layerName != '')
		{
			var l = _layerManager.GetLayer(layerName);
			if(l != null)
			{
				l.ShowLayer();
			}
		}
	};
	
	this.LoadMap = function(mapId){
		_mapId = mapId;

		_bingMapKey = Utilities.GetQuerystring('bingMapKey', '');
	    while(window.Microsoft == undefined){}
		
		//Add entityId as a property of all shape objects.
	    Microsoft.Maps.Pushpin.prototype.entityId = -1;
		Microsoft.Maps.Polygon.prototype.entityId = -1;
		Microsoft.Maps.Polyline.prototype.entityId = -1;
		
	    var options = {
	    	credentials : _bingMapKey,
	    	zoom: parseInt(Utilities.GetQuerystring('zoom', '1')),
	    	center: new MM.Location(parseFloat(Utilities.GetQuerystring('lat', '0')),parseFloat(Utilities.GetQuerystring('lon', '0'))),
	    	showDashboard : false, 
			showScalebar: false, 
			showCopyright: false,
			enableSearchLogo: false, 
			enableClickableLogo: false };

		_map = new MM.Map(document.getElementById(_mapId), options);	

		_layerManager = new LayerManager(_map);		
		
		//Add handler for the map click event.
        MM.Events.addHandler(_map, 'dblclick', function(e){
        	if (e.targetType = "map") {
        	 	var p = new MM.Point(e.getX(), e.getY());
                var c = e.target.tryPixelToLocation(p);        	
                var z = _map.getZoom();
				_map.setView({ zoom: z + 1, center: c });
            }
        });
		
		if(window.BingMapsInterlop)
		{
			MM.Events.addHandler(_map, "viewchangeend", function(e){
					var c = _map.getCenter();
					var z = _map.getZoom();
					var b = _map.getBounds();
					window.BingMapsInterlop.mapMovedEvent(c.latitude, c.longitude, z, 
						b.getNorth(), b.getEast(), b.getSouth(), b.getWest());
				});
			
			window.BingMapsInterlop.mapLoaded();
		}
		
		setMapDimensions();
	};
	
	this.Pan = function(dx, dy){
		_map.setView({ centerOffset : new MM.Point(dx, dy)});
	};
	
	this.SetCenterAndZoom = function(c, z) {
		_map.setView({ center : c, zoom : z});
	};
	
	//���þ��붥��
	this.SetHeading = function(h){
		_map.setView({ heading : h});
	};
	
	//������ʾ��ͼ
	this.SetMapView = function(view) {
		_map.setView({ bounds : view});
	};
	
	//������ʾ��ͼ��ʾ��ʽ(Auto, Road, Aerial, Birdseye)
	this.SetMapStyle = function(style) {
		_map.setMapType(style);
	};
	
	//��ʾ��Ϣ��
	this.ShowInfobox = function(entity){
		if(_infobox != null){
			_map.entities.remove(_infobox);
		}
		
		if(entity.title != null && entity.title != undefined && entity.title != ''){
			var center = null;
							
			if(entity.getLocation != null){
				center = entity.getLocation();
			}else{//calculate center of polyline or polygon
				center = Utilities.GetCenter(entity.getLocations());
			}
			
			if(center != null){						
				var title = (entity.title.length > 23) ? Utilities.TruncateText(entity.title, 20) : entity.title;
				
				// Create an info box 
				var infoboxOptions = {width:200, 
						  height: 30, 
						  title: title, 
						  showPointer: true, 
						  titleClickHandler: function(){
							if(window.BingMapsInterlop){
								window.BingMapsInterlop.entityClicked(entity.layerName, entity.entityId);
							}		
						  }, 
						  offset: new MM.Point(0,20),
						  visible:true,
						  zIndex:1000};
				
				_infobox = new MM.Infobox(center, infoboxOptions);

				// Add the info box to the map
				_map.entities.push(_infobox);
			}
		}
	};
	
	//���õ�ͼ��С
	this.ZoomIn = function(x, y){
		var z = _map.getZoom();
		_map.setView({ zoom: z + 1 });
	};
	
	//���õ�ͼ�Ŵ�
	this.ZoomOut = function(x, y){
		var z = _map.getZoom();
		_map.setView({ zoom: z - 1 });
	};
	
	//���õ�ͼ��ʾ�ߴ�
	function setMapDimensions(){
        _map.setOptions({width : window.innerWidth, height:window.innerHeight});		
	}
};

var Layer = function(name, map){
	this.Name = name;
	
	var entities = new MM.EntityCollection(),
	_map = map;
	
	this.AddEntities = function(data){
		if(data != null){
			var i = data.length - 1;
			if(i >= 0)
			{					
				do{
					data[i].Entity.entityId = data[i].EntityId;
					data[i].Entity.layerName = name;
					
					if(data[i].title != null && data[i].title != undefined && data[i].title != ''){
						data[i].Entity.title = data[i].title;
						
						// Add a handler for the pushpin click event.
						MM.Events.addHandler(data[i].Entity, 'click', function(e){
							BingMapsAndroid.ShowInfobox(e.target);
						});
					}	

					entities.push(data[i].Entity);							
				}while(i--)
			}
		}
	};
	
	this.ClearLayer = function(){
		entities.clear();
	};
	
	this.HideLayer = function(){
		entities.setOptions({visible:false});
	};
	
	this.ShowLayer = function(){
		entities.setOptions({visible:true});
	};

	function init(){
		_map.entities.push(entities);	
	}
	init();
};

var LayerManager = function(map){
	var _layers = [],
	_map = map;

	this.AddLayer = function(name){
		var layer = new Layer(name, _map);
		_layers.push(layer);
	};
	
	this.DeleteLayer = function(name){
		var i = getLayerIndexByName(name);
		
		if(i != -1)
		{
			_map.entities.removeAt(i);
			_layers.splice(i,1);
		}
	};
	
	this.DeleteAllLayers = function(){
		_map.entities.clear();
		_layers = [];
	};
	
	this.GetLayer = function(name){
		var i = getLayerIndexByName(name);
		return (i != -1)?_layers[i]:null;
	};
	
	function getLayerIndexByName(name){
		for(var i=0;i<_layers.length;i++)
		{
			if(_layers[i].Name == name){
				return i;
			}
		}
		
		return -1;
	}
};

var Utilities = new function(){
	this.GetQuerystring = function(key, _default) {
		if (_default == null) {
			_default = '';
		}

		var regex = new RegExp("[\\?&]" + key + "=([^&#]*)"),
			qs = regex.exec(window.location.href);

		if (qs == null || qs.length < 2) {
			return _default;
		}
		else {
			return qs[1];
		}
	};
	
	this.TruncateText = function(text, len) {
		if (text.length > len && len > 3) {
			text = text.substring(0, len - 2) + '...';
		}

		return text;
	};

	this.GetCenter = function(locations){
		if(locations != null){
			var i = locations.length - 1;
			if(i >= 0)
			{	
				var maxLat = -90, maxLon = -180, minLat = 90, minLon = 180;			
				do{
					if(locations[i].latitude > maxLat){
						maxLat = locations[i].latitude;
					}
					if(locations[i].longitude > maxLon){
						maxLon = locations[i].longitude;
					}
					if(locations[i].latitude < minLat){
						minLat = locations[i].latitude;
					}
					if(locations[i].longitude < minLon){
						minLon = locations[i].longitude;
					}
				}while(i--)
				
				return new MM.Location((maxLat+minLat)*0.5,(maxLon+minLon)*0.5);
			}
		}
		
		return null;
	};
};

Microsoft.Maps.moduleLoaded('mapModule');