<template>
  <div class="app-container">
    <el-container>
      <el-header><span style="font-size: 40px">{{ data.libraryName }}</span></el-header>
      <el-main class="hidden-sm-and-down" style="line-height: 10px;">
        <el-row type="flex" justify="center">
          <el-col :span="2">
            <el-popover
              placement="bottom-start"
              width="200"
              trigger="hover"
              content="刷新媒体库"
            >
              <el-button slot="reference" type="primary" icon="el-icon-refresh-left" circle @click="openMediaRefreshDialog(data.libraryId)" />
            </el-popover>
          </el-col>
          <el-col :span="2">
            <el-popover
              placement="bottom-start"
              width="200"
              trigger="hover"
              content="刷新豆瓣信息"
            >
              <el-button slot="reference" type="primary" icon="el-icon-refresh-right" circle @click="openDoubanRefreshDialog(data.libraryId)" />
            </el-popover>
          </el-col>
        </el-row>
      </el-main>
      <el-main>
        <el-row :gutter="10">
          <el-col v-for="(item, index) in data.videos" :key="index" :xs="24" :sm="12" :lg="6">
            <el-container>
              <el-main>
                <vue-hover-mask class="vue-hover-mask" @click="handleClick(item.id, data.libraryType)">
                  <div style="width: 200px;height: 300px">
                    <el-image
                      v-if="item.imgPath"
                      :src="getPicPath(item.imgPath)"
                      style="width: 100%;height: 100%;"
                    />
                    <svg-icon v-else icon-class="videotape" style="color: white;height: 30%;width: 30%;text-align:center;margin-top: 100px;" />
                  </div>
                  <template v-slot:action>
                    <svg-icon icon-class="play" style="height: 50%;width: 50%; text-align:center;top: 65px;position: relative" />
                  </template>
                </vue-hover-mask></el-main>
              <el-footer style="margin-top:-40px;margin-left: -30px;position: relative">
                <el-link style="overflow: hidden" @click="handleClick(item.id, data.libraryType)">{{ item.name }}</el-link>
              </el-footer>
            </el-container>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import VueHoverMask from '@/components/HoverMask/VueHoverMask'
import 'element-ui/lib/theme-chalk/display.css'
import { getMediaInfoById, refreshMediaInfo, refreshDoubanInfo } from '@/api/user'
export default {
  name: 'Media',
  components: {
    VueHoverMask
  },
  data() {
    return {
      listLoading: true,
      data: {
        libraryName: '',
        libraryType: 0,
        libraryId: 0,
        videos: [
          {
            id: 0,
            name: '',
            location: '',
            imgPath: ''
          }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getPicPath: function(e) {
      if (e == null || e === '') {
        return ''
      }
      return process.env.VUE_APP_BASE_API + '/' + e
    },
    getList() {
      this.listLoading = true
      getMediaInfoById(this.$route.meta.id).then(response => {
        this.data = response.data
        this.listLoading = false
      })
    },
    handleClick: function(id, type) {
      this.$router.push({
        name: 'infos',
        params: {
          id: id,
          type: type
        }
      })
    },
    openDoubanRefreshDialog: function(e) {
      this.$confirm('此操作将刷新豆瓣信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        refreshDoubanInfo(e).then(response => {
          this.$message({
            type: 'success',
            message: '刷新成功，数据导入需要3-5分钟!'
          })
          this.$router.go(0)
        })
      })
    },
    openMediaRefreshDialog: function(e) {
      this.$confirm('此操作将刷新媒体库文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        refreshMediaInfo(e).then(response => {
          this.$message({
            type: 'success',
            message: '刷新成功，数据导入需要3-5分钟!'
          })
          this.$router.go(0)
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .el-header, .el-footer {
    color: #333;
    text-align: center;
    line-height: 60px;
  }
  .el-main {
    color: #333;
    text-align: center;
    line-height: 160px;
  }
  .el-col {
    border-radius: 4px;
    margin-bottom: 32px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #fb7299;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .vue-hover-mask{
    background: #fb7299;
  }
</style>
