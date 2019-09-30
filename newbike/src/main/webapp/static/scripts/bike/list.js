$(function() {
	$('.table-sort').DataTable({
				order : [ [ 1, 'asc' ] ],
				//加载后台的数据，然后显示在表格（table）里面
				ajax : {
					url : "/bikes",
					type : 'GET',
					dataSrc : ""
				},
				columns : [  {
					//0
                    data : "id"
                }, {
					//1
					data : "id"
				}, {
					//2
					data : "status",
					defaultContent : ""
				}, {
					//3
					data : "latitude",
					defaultContent : ""
				}, {
					//4
					data : "longitude",
					defaultContent : ""
				}, {
					//5
					data : "id",
					defaultContent : ""
				} ],
				columnDefs : [{
					targets : [ 0 ],
					orderable : false,
					render : function(id, type, row, meta) {
						return '<input id="input-' + id
								+ '" type="checkbox" name="ids" value=' + id
								+ '><label for="input-' + id + '"></label>';
					}
				}, {
                    targets: [5],
                    render: function(data, type, row, meta) {
                        return '<a title="编辑" href="javascript:;" onclick="bike_edit('+ data +')" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a><a title="删除" href="javascript:;" onclick="bike_del(' + data +')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a>'
                    }
                }]
			});
});
