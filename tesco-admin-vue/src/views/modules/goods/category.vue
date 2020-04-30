<template>
  <div>
    <el-switch v-model="draggable" active-text="开启拖拽" inactive-text="关闭拖拽"></el-switch>
    <el-button v-if="draggable" @click="batchSave">批量保存</el-button>
    <el-tree
      :data="menus"
      :props="defaultProps"
      :expand-on-click-node="false"
      show-checkbox
      node-key="categoryId"
      :default-expanded-keys="expandedKey"
      :draggable="draggable"
      :allow-drop="allowDrop"
      @node-drop="handleDrop"
      ref="menuTree"
    >
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level <=2"
            type="text"
            size="mini"
            @click="() => append(data)"
          >新增</el-button>
          <el-button type="text" size="mini" @click="edit(data)">修改</el-button>
          <el-button
            v-if="node.childNodes.length==0"
            type="text"
            size="mini"
            @click="() => remove(node, data)"
          >删除</el-button>
        </span>
      </span>
    </el-tree>
    <el-button type="danger" @click="batchDelete">批量删除</el-button>
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="30%"
      :close-on-click-modal="false"
    >
      <el-form :model="category">
        <el-form-item label="分类名称">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="计量单位">
          <el-input v-model="category.productUnit" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitData">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//import 《组件名称》 from '《组件路径》';
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  props: {},
  data() {
    return {
      pCid: [],
      draggable: false,
      updateNodes: [],
      maxLevel: 0,
      title: "",
      dialogType: "",
      category: {
        name: "",
        parentCid: 0,
        categoryLevel: 0,
        showStatus: 1,
        sort: 0,
        productUnit: "",
        icon: "",
        categoryId: null
      },
      dialogVisible: false,
      menus: [],
      expandedKey: [],
      defaultProps: {
        children: "children",
        label: "name"
      }
    };
  },

  //计算属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    //获取三级分类树形图
    getMenus() {
      this.$http({
        url: this.$http.adornUrl("/goods/category/list/tree"),
        method: "get"
      }).then(({ data }) => {
        console.log("成功获取到分类数据", data.data);
        this.menus = data.data;
      });
    },
    //发起新增
    append(data) {
      console.log("新增的数据：", data);
      this.dialogType = "add";
      this.title = "添加分类";
      this.dialogVisible = true;
      this.category.parentCid = data.categoryId;
      this.category.categoryLevel = data.categoryLevel * 1 + 1;
      this.category.categoryId = null;
      this.category.name = "";
      this.category.icon = "";
      this.category.productUnit = "";
      this.category.sort = 0;
      this.category.showStatus = 1;
    },
    //发起修改
    edit(data) {
      console.log("修改的数据：", data);
      this.dialogType = "edit";
      this.title = "修改分类";
      this.dialogVisible = true;
      //发送请求获取当前节点最新的数据
      this.$http({
        url: this.$http.adornUrl(`/goods/category/info/${data.categoryId}`),
        method: "get"
      }).then(({ data }) => {
        //请求成功
        console.log("回显的数据：", data);
        this.category.name = data.category.name;
        this.category.categoryId = data.category.categoryId;
        this.category.icon = data.category.icon;
        this.category.productUnit = data.category.productUnit;
        // this.category.parentCid = data.category.parentCid;
        // this.category.categoryLevel = data.category.categoryLevel;
        // this.category.sort = data.category.sort;
        // this.category.showStatus = data.category.showStatus;
      });
    },
    remove(node, data) {
      var ids = [data.categoryId];
      this.$confirm(`是否删除分类【${data.name}】?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$http({
            url: this.$http.adornUrl("/goods/category/delete"),
            method: "post",
            data: this.$http.adornData(ids, false)
          }).then(({ data }) => {
            this.$message({
              message: "分类删除成功",
              type: "success"
            });
            //刷新出新的菜单
            this.getMenus();
            //设置需要默认展开的菜单
            this.expandedKey = [node.parent.data.categoryId];
          });
        })
        .catch(() => {});
      console.log("remove", node, data);
    },
    //新增、修改分类，提交数据
    submitData() {
      if (this.dialogType == "add") {
        this.addCategory();
      }
      if (this.dialogType == "edit") {
        this.editCategory();
      }
    },
    //添加三级分类
    addCategory() {
      console.log("提交的三级分类数据", this.category);
      this.$http({
        url: this.$http.adornUrl("/goods/category/save"),
        method: "post",
        data: this.$http.adornData(this.category, false)
      }).then(({ data }) => {
        this.$message({
          message: "分类新增成功",
          type: "success"
        });
        //关闭对话框
        this.dialogVisible = false;
        //刷新出新的菜单
        this.getMenus();
        //设置需要默认展开的菜单
        this.expandedKey = [this.category.parentCid];
      });
    },
        countNodeLevel(node) {
      //找到所有子节点，求出最大深度
      if (node.childNodes != null && node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          if (node.childNodes[i].level > this.maxLevel) {
            this.maxLevel = node.childNodes[i].level;
          }
          this.countNodeLevel(node.childNodes[i]);
        }
      }
    },
    //修改三级分类
    editCategory() {
      var { categoryId, name, icon, productUnit } = this.category;
      this.$http({
        url: this.$http.adornUrl("/goods/category/update"),
        method: "post",
        data: this.$http.adornData({ categoryId, name, icon, productUnit }, false)
      }).then(({ data }) => {
        this.$message({
          message: "分类修改成功",
          type: "success"
        });
        //关闭对话框
        this.dialogVisible = false;
        //刷新出新的菜单
        this.getMenus();
        //设置需要默认展开的菜单
        this.expandedKey = [this.category.parentCid];
      });
    },
    //批量删除
    batchDelete() {
      let categoryIds = [];
      let nameList = [];
      let checkedNodes = this.$refs.menuTree.getCheckedNodes();
      console.log("选中的元素", checkedNodes);
      for (let i = 0; i < checkedNodes.length; i++) {
        categoryIds.push(checkedNodes[i].categoryId);
        nameList.push(checkedNodes[i].name)
      }
      this.$confirm(`是否批量删除分类【${nameList}】？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$http({
            url: this.$http.adornUrl("/goods/category/delete"),
            method: "post",
            data: this.$http.adornData(categoryIds, false)
          }).then(({ data }) => {
            this.$message({
              message: "分类批量删除成功",
              type: "success"
            });
            this.getMenus();
          });
        })
        .catch(() => {});
    },
    //拖拽，批量保存
    batchSave() {
      this.$http({
        url: this.$http.adornUrl("/goods/category/update/sort"),
        method: "post",
        data: this.$http.adornData(this.updateNodes, false)
      }).then(({ data }) => {
        this.$message({
          message: "拖拽成功",
          type: "success"
        });
        //刷新出新的菜单
        this.getMenus();
        //设置需要默认展开的菜单
        this.expandedKey = this.pCid;
        this.updateNodes = [];
        this.maxLevel = 0;
      });
    },
    //允许拖拽
    allowDrop(draggingNode, dropNode, type) {
      //被拖动的当前节点以及所在的父节点总层数不能大于3
      //被拖动的当前节点总层数
      console.log("allowDrop:", draggingNode, dropNode, type);
      this.countNodeLevel(draggingNode);
      let deep = Math.abs(this.maxLevel - draggingNode.level) + 1;
      console.log("深度：", deep);
      if (type == "inner") {
        return deep + dropNode.level <= 3;
      } else {
        return deep + dropNode.parent.level <= 3;
      }
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      console.log("handleDrop: ", draggingNode, dropNode, dropType);
      //1、当前节点最新的父节点id
      let pCid = 0;
      let siblings = null;
      if (dropType == "before" || dropType == "after") {
        pCid =
          dropNode.parent.data.categoryId == undefined
            ? 0
            : dropNode.parent.data.categoryId;
        siblings = dropNode.parent.childNodes;
      } else {
        pCid = dropNode.data.categoryId;
        siblings = dropNode.childNodes;
      }
      this.pCid.push(pCid);
      //2、当前拖拽节点的最新顺序，
      for (let i = 0; i < siblings.length; i++) {
        if (siblings[i].data.categoryId == draggingNode.data.categoryId) {
          //如果遍历的是当前正在拖拽的节点
          let categoryLevel = draggingNode.level;
          if (siblings[i].level != draggingNode.level) {
            //当前节点的层级发生变化
            categoryLevel = siblings[i].level;
            //修改他子节点的层级
            this.updateChildNodeLevel(siblings[i]);
          }
          this.updateNodes.push({
            categoryId: siblings[i].data.categoryId,
            sort: i,
            parentCid: pCid,
            categoryLevel: categoryLevel
          });
        } else {
          this.updateNodes.push({ categoryId: siblings[i].data.categoryId, sort: i });
        }
      }
      //3、当前拖拽节点的最新层级
      console.log("updateNodes", this.updateNodes);
    },
    updateChildNodeLevel(node) {
      if (node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          var cNode = node.childNodes[i].data;
          this.updateNodes.push({
            categoryId: cNode.categoryId,
            categoryLevel: node.childNodes[i].level
          });
          this.updateChildNodeLevel(node.childNodes[i]);
        }
      }
    },
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
    this.getMenus();
  },
  //生命周期 - 挂载完成（可以访问DOM元素）
  mounted() {},
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {} //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style scoped>
</style>
