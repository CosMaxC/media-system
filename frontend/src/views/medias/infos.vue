<template>
  <el-container>
    <el-main>
      <!-- 标题-->
      <el-row type="flex">
        <el-col class="hidden-sm-and-down" :lg="1" />
        <el-col v-if="data.doubanResponse && data.doubanResponse.doubanId > 0" :xs="24" :sm="24" :lg="16">
          <h1>{{ data.doubanResponse.cnName }} ({{ data.doubanResponse.year }})</h1>
        </el-col>
        <el-col v-else-if="data.movieInfo && data.movieInfo.movieId > 0" :xs="24" :sm="24" :lg="16">
          <h1>{{ data.movieInfo.movieName }}</h1>
        </el-col>
        <el-col v-else-if="data.seriesInfo && data.seriesInfo.seriesId > 0" :xs="24" :sm="24" :lg="16">
          <h1>{{ data.seriesInfo.seriesName }}</h1>
        </el-col>
      </el-row>
      <!-- 大屏视频播放 -->
      <el-row class="hidden-sm-and-down" type="flex">
        <el-col :lg="1" />
        <el-col :span="20">
          <video-player
            ref="videoPlayer"
            class="video-player vjs-custom-skin"
            :playsinline="true"
            :options="playerOptions"
          />
        </el-col>
      </el-row><br>
      <!-- 大屏剪切板 -->
      <el-row class="hidden-sm-and-down" type="flex">
        <el-col :lg="1" />
        <el-col :span="13">
          <el-input v-model="inputData" placeholder="Please input" />
        </el-col>
        <el-col :span="6">
          <el-button v-clipboard:copy="inputData" v-clipboard:success="clipboardSuccess" type="primary" icon="el-icon-document">
            复制直链
          </el-button>
          <el-button type="primary" @click="openApplication('potplayer')">
            打开potplayer
          </el-button>
        </el-col>
      </el-row>
      <!-- 小屏视频播放 -->
      <el-row class="hidden-md-and-up" type="flex">
        <el-col :span="24">
          <video-player
            ref="videoPlayer"
            class="video-player vjs-custom-skin"
            :playsinline="true"
            :options="playerOptions"
          />
        </el-col>
      </el-row>
      <!-- 小屏剪切板 -->
      <el-row class="hidden-md-and-up" type="flex">
        <el-col :span="24">
          <el-input v-model="inputData" placeholder="Please input" />
        </el-col>
      </el-row>
      <el-row class="hidden-md-and-up" type="flex">
        <el-col :span="24">
          <el-button v-clipboard:copy="inputData" v-clipboard:success="clipboardSuccess" type="primary" icon="el-icon-document">
            复制直链
          </el-button>
        </el-col>
      </el-row>
      <!-- 播放列表 -->
      <el-row type="flex" :gutter="20">
        <el-col :xs="24" :sm="24" :lg="{span: 16, offset: 1}"><h3>播放列表</h3></el-col>
      </el-row>
      <el-row type="flex" :gutter="2">
        <el-col class="hidden-sm-and-down" :span="1" />
        <el-col :span="20">
          <el-row>
            <el-col v-if="data.movieInfo && data.movieInfo.movieId > 0" :xs="8" :sm="8" :lg="3">
              <el-button type="primary">第1集</el-button>
            </el-col>
            <el-col v-for="(item, index) in data.seriesEpisodes" v-else-if="data.seriesInfo && data.seriesInfo.seriesId > 0" :key="index" :xs="8" :sm="8" :lg="3">
              <el-button :type="getSize(getVidPath(item.episodeLocation))" @click="handlerPlay(item.episodeLocation)">第{{ item.episode }}集</el-button>
            </el-col>
          </el-row>
        </el-col>
      </el-row><br>
      <!-- 大屏浏览器 豆瓣信息 -->
      <el-row v-if="data.doubanResponse && data.doubanResponse.doubanId > 0" class="hidden-sm-and-down" type="flex" :gutter="20">
        <el-col :span="1" />
        <el-col :span="5"><el-image :src="getPicPath(data.doubanResponse.avatarPath)" /></el-col>
        <el-col :span="15">
          <el-card class="box-card" shadow="never">
            <div class="card-item">
              <el-row>
                <el-col :span="3">豆瓣评分:</el-col>
                <el-col :span="10"><el-rate :value="data.doubanResponse.starCount * 0.1" disabled show-score text-color="#ff9900" score-template="{value}" /></el-col>
              </el-row>
            </div>
            <div class="card-item">
              导演: <span v-for="(item, index) in data.doubanResponse.directorList" :key="index" class="card-span"><el-link target="_blank" :href="getStarLink(item.starId)" type="primary">{{ item.starName }}</el-link> / </span>
            </div>
            <div class="card-item">
              编剧: <span v-for="(item, index) in data.doubanResponse.writerList" :key="index" class="card-span"><el-link target="_blank" :href="getStarLink(item.starId)" type="primary">{{ item.starName }}</el-link> / </span>
            </div>
            <div class="card-item">
              主演:<span v-for="(item, index) in data.doubanResponse.starList" :key="index" class="card-span"><el-link target="_blank" :href="getStarLink(item.starId)" type="primary">{{ item.starName }}</el-link> / </span>
            </div>
            <div class="card-item">
              制片国家/地区: <span class="card-span">{{ data.doubanResponse.madeBy }}</span>
            </div>
            <div class="card-item">
              别名: <span class="card-span">{{ data.doubanResponse.engName }}</span>
            </div>
            <div class="card-item">
              上映日期: <span class="card-span">{{ data.doubanResponse.onTime }}</span>
            </div>
            <div class="card-item">
              片长: <span class="card-span">{{ data.doubanResponse.costTime }}</span>
            </div>
            <div class="card-item">
              豆瓣链接: <span class="card-span"><el-link type="primary" target="_blank" :href="data.doubanResponse.doubanLink">{{ data.doubanResponse.doubanId }}</el-link></span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="3" />
      </el-row>
      <!-- 小屏浏览器 豆瓣信息 -->
      <el-row v-if="data.doubanResponse && data.doubanResponse.doubanId > 0" class="hidden-md-and-up" type="flex" :gutter="20">
        <el-col :span="24"><el-image style="width: 100%; height: 100%" :src="getPicPath(data.doubanResponse.avatarPath)" /></el-col>
      </el-row>
      <el-row v-if="data.doubanResponse && data.doubanResponse.doubanId > 0" class="hidden-md-and-up" type="flex" :gutter="20">
        <el-col :span="24">
          <el-card shadow="never">
            <div class="card-item">
              <el-row>
                <el-col :span="7" style="color: #909399">豆瓣评分:</el-col>
                <el-col :span="15"><el-rate :value="data.doubanResponse.starCount * 0.1" disabled show-score text-color="#ff9900" score-template="{value}" /></el-col>
              </el-row>
            </div>
            <div class="card-item">
              导演: <span v-for="(item,index) in data.doubanResponse.directorList" :key="index" class="card-span"><el-link target="_blank" :href="getStarLink(item.starId)" type="primary">{{ item.starName }}</el-link> / </span>
            </div>
            <div class="card-item">
              编剧: <span v-for="(item,index) in data.doubanResponse.writerList" :key="index" class="card-span"><el-link target="_blank" :href="getStarLink(item.starId)" type="primary">{{ item.starName }}</el-link> / </span>
            </div>
            <div class="card-item">
              主演:<span v-for="(item,index) in data.doubanResponse.starList" :key="index" class="card-span"><el-link target="_blank" :href="getStarLink(item.starId)" type="primary">{{ item.starName }}</el-link> / </span>
            </div>
            <div class="card-item">
              制片国家/地区: <span class="card-span">{{ data.doubanResponse.madeBy }}</span>
            </div>
            <div class="card-item">
              别名: <span class="card-span">{{ data.doubanResponse.engName }}</span>
            </div>
            <div class="card-item">
              上映日期: <span class="card-span">{{ data.doubanResponse.onTime }}</span>
            </div>
            <div class="card-item">
              片长: <span class="card-span">{{ data.doubanResponse.costTime }}</span>
            </div>
            <div class="card-item">
              豆瓣链接: <span class="card-span"><el-link type="primary" target="_blank" :href="data.doubanResponse.doubanLink">{{ data.doubanResponse.doubanId }}</el-link></span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="3" />
      </el-row>
    </el-main>
  </el-container>
</template>
<script>
import 'element-ui/lib/theme-chalk/display.css'
import { getMovieInfoById, getSeriesInfoById } from '@/api/user'
import clipboard from '@/directive/clipboard/index.js' // use clipboard by v-directive
import { videoPlayer } from 'vue-video-player'
require('video.js/dist/video-js.css')
require('vue-video-player/src/custom-theme.css')
export default {
  name: 'Infos',
  components: {
    videoPlayer
  },
  directives: {
    clipboard
  },
  data() {
    return {
      listLoading: true,
      // videojs options
      playerOptions: {
        playbackRates: [0.7, 1.0, 1.5, 2.0], // 播放速度
        autoplay: false, // 如果true,浏览器准备好时开始回放。
        muted: false, // 默认情况下将会消除任何音频。
        loop: false, // 导致视频一结束就重新开始。
        preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
        language: 'zh-CN',
        aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        sources: [{
          type: '', // 这里的种类支持很多种：基本视频格式、直播、流媒体等，具体可以参看git网址项目
          src: '' // url地址
        }],
        poster: '../../static/images/test.jpg', // 你的封面地址
        // width: document.documentElement.clientWidth, //播放器宽度
        notSupportedMessage: '此视频暂无法播放，请稍后再试', // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
        controlBar: {
          timeDivider: true,
          durationDisplay: true,
          remainingTimeDisplay: false,
          fullscreenToggle: true // 全屏按钮
        }
      },
      inputData: '',
      activeName: 'directly',
      data: {
        movieInfo: {
          movieId: 0,
          movieName: '',
          movieLocation: ''
        },
        seriesInfo: {
          seriesId: 0,
          seriesName: '',
          seriesLocation: ''
        },
        seriesEpisodes: [
          {
            seriesId: 0,
            season: 0,
            episode: 0,
            episodeLocation: ''
          }
        ],
        doubanResponse: {
          doubanId: 0,
          starCount: 0,
          score: 0.0,
          madeBy: '',
          year: 2020,
          costTime: '',
          onTime: '',
          engName: '',
          doubanLink: '',
          avatarPath: '',
          posterPath: '',
          subType: 0,
          cnName: '',
          brief: '',
          directorList: [
            {
              starId: 0,
              starName: '',
              starImg: '',
              starBrief: ''
            }
          ],
          writerList: [
            {
              starId: 0,
              starName: '',
              starImg: '',
              starBrief: ''
            }
          ],
          starList: [
            {
              starId: 0,
              starName: '',
              starImg: '',
              starBrief: ''
            }
          ]
        }
      }
    }
  },
  created() {
    this.getList()
  },
  activated() {
    this.getList()
  },
  methods: {
    getStarLink: function(e) {
      return 'https://movie.douban.com/celebrity/' + e
    },
    getSize: function(e) {
      if (e === this.playing) {
        return 'primary'
      }
      return 'default'
    },
    clipboardSuccess() {
      this.$message({
        message: 'Copy successfully',
        type: 'success',
        duration: 1500
      })
    },
    openApplication(e) {
      window.location.href = e + '://' + this.inputData
      setTimeout(function() {
        if (e === 'potplayer') {
          window.open('https://potplayer.en.softonic.com/')
        }
        if (e === 'nplayer') {
          window.open('https://potplayer.en.softonic.com/')
        }
      }, 3000)
    },
    handlerPlay: function(e) {
      this.playing = this.getVidPath(e)
      this.inputData = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + this.getVidPath(e)
    },
    getPicPath: function(e) {
      if (e == null || e === '') {
        return ''
      }
      return this.getVisitUrl() + '/' + e
    },
    getVidPath: function(e) {
      if (e == null || e === '') {
        return ''
      }
      return this.getVisitUrl() + e
    },
    getPort() {
      const url = process.env.VUE_APP_BASE_API
      var list = url.split(':', 3)
      return list[2]
    },
    getVisitUrl: function() {
      // return window.location.protocol + '//' + window.location.hostname + ':' + this.getPort()
      return process.env.VUE_APP_BASE_API
    },
    getList() {
      const type = this.$route.params.type
      this.listLoading = true
      if (type === 0 || type === '0') {
        // 电影类型请求电影api
        getMovieInfoById(this.$route.params.id).then(response => {
          this.data = response.data
          this.inputData = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + this.getVidPath(response.data.movieInfo.movieLocation)
          this.playing = this.getVidPath(response.data.movieInfo.movieLocation)
          this.playerOptions.sources = [{
            type: 'video/mp4', // 这里的种类支持很多种：基本视频格式、直播、流媒体等，具体可以参看git网址项目
            src: this.getVidPath(response.data.movieInfo.movieLocation) // url地址
          }]
        })
      }
      if (type === 1 || type === '1') {
        // 电视剧类型请求电视剧api
        getSeriesInfoById(this.$route.params.id).then(response => {
          this.data = response.data
          this.inputData = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + this.getVidPath((response.data.seriesEpisodes)[0].episodeLocation)
          this.playing = this.getVidPath((response.data.seriesEpisodes)[0].episodeLocation)

          this.playerOptions.sources = [{
            type: 'video/mp4', // 这里的种类支持很多种：基本视频格式、直播、流媒体等，具体可以参看git网址项目
            src: this.getVidPath((response.data.seriesEpisodes)[0].episodeLocation) // url地址
          }]
        })
      }
      this.listLoading = false
    }
  }
}
</script>

<style lang="scss" scoped>
  .el-header, .el-footer {
    background-color: #ffffff;
    color: #333;
    text-align: center;
    line-height: 60px;
  }
  .el-main {
    background-color: #ffffff;
    color: #333;
    /*text-align: center;*/
    /*line-height: 160px;*/
  }

  body > .el-container {
    margin-bottom: 40px;
  }
  .el-col {
    border-radius: 4px;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .card-text {
    font-size: 16px;
  }
  .card-span {
    font-size: 16px;
    color: #000000;
  }
  .card-item {
    padding: 9px 0;
  }

</style>
