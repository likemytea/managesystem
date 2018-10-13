    var vm = new Vue({
        el: '#leaveTaskListTable',
        data: {
          listData:[],
          page: 1,//当前页码
          pageSize: 5,//每页条数
          total:0,//总数
          pageData: {
            curPage: 3,//赋值为3没有任何意义，可以随意填写
            pageSize: 4,//赋值为4没有任何意义，可以随意填写
            total: 0,
            totalPage: 0,
            pageIndex: [],
            itemStart: 0,
            itemEnd: 0
          }
        },
        methods:{
          listItems: function () {//列出需要的数据
            var returnData =getData(this.page,this.pageSize);
            this.listData = returnData['data'];
            this.total=returnData['total'];
            this.setPageList(this.total, this.page, this.pageSize);
          },
          execTask:function ($taskid){
        	    var $beok = false;
        	    if (confirm('同意点击确定，不同意选择取消')){
        	    	$beok = true;
        	    }else{
        	    	$beok = false;
        	    }
     	        var $res = '';
     		   	 $.ajax({
     			     type:'post',
     			     url:'http://localhost:8060/workflow/leave/complete',
     			     async: false,
     			     data:{taskid:$taskid,keys:'beok',values:$beok,types:'B'},
     			     cache:false,
     			     dataType:'json',
     			     success:function(data){
     			         $res = data.totalCount;
     			     },
     	            error:function(XMLHttpRequest, textStatus, errorThrown) {
     	                $res = XMLHttpRequest.responseText;
     	            }
     			 });
     		  	 if($res=='finished'){
     		   		 alert('执行完毕！');
     		   	 }else if($res=='paramError'){
     		   		alert('参数错误，执行失败，请重新输入！');
     		   	 }else{
     		   		alert('系统异常，请联系运维人员！');
     		   	 }
         },
          deleteItem:function ($id) {//这里可以删除数据
            alert('删除第'+$id+'条数据！');
          },
          setPageList: function (total, page, pageSize) {
          total = parseInt(total);
          var curPage = parseInt(page);
          pageSize = parseInt(pageSize);
          var totalPage = Math.ceil(total / pageSize);
          var itemStart = (curPage - 1) * pageSize + 1;
          if (curPage == totalPage) {
            itemEnd = total;
          } else {
            itemEnd = curPage * pageSize;
          }
          var pageIndex = [];
          if (curPage >= 1 && curPage <= totalPage) {
            if (totalPage < 5) {//5页以内
              for (var $i = 1; $i <= totalPage; $i++) {
                pageIndex.push($i);
              }
            } else {//大于5页
              if (curPage == 1) {
                pageIndex = [curPage, curPage + 1, curPage + 2, curPage + 3, curPage + 4];
              } else if (curPage == 2) {
                pageIndex = [curPage - 1, curPage, curPage + 1, curPage + 2, curPage + 3];
              } else if (curPage == totalPage - 1) {
                pageIndex = [curPage - 3, curPage - 2, curPage - 1, curPage, totalPage];
              } else if (curPage == totalPage) {
                pageIndex = [curPage - 4, curPage - 3, curPage - 2, curPage - 1, curPage];
              } else {
                pageIndex = [curPage - 2, curPage - 1, curPage, curPage + 1, curPage + 2];
              }
            }
          }
   
          this.pageData.curPage = curPage;
          this.pageData.pageSize = pageSize;
          this.pageData.total = total;
          this.pageData.totalPage = totalPage;
          this.pageData.pageIndex = pageIndex;
          this.pageData.itemStart = itemStart;
          this.pageData.itemEnd = itemEnd;
        },
        changeCurPage: function (page, pageSize) {//换页
          this.page = page;
          this.pageSize = pageSize;
          this.listItems();
        },
        goPage: function (pageSize, totalPage) {//跳转页
          var pageIndex = this.$refs.goPage.value;
          if (pageIndex <= 0) {
            pageIndex = 1;
            this.$refs.goPage.value = 1;
          } else if (pageIndex >= totalPage) {
            pageIndex = totalPage;
            this.$refs.goPage.value = totalPage;
          }
          this.changeCurPage(pageIndex, pageSize);
        }
        }
      });