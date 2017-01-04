(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('statisticsTopUsers', {
            parent: 'app',
            url: '/statistics/users',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Statistics'
            },
            views: {
                'content@': {
                    templateUrl: 'app/statisticsTopUsers/statisticsTopUsers.html',
                    controller: 'StatisticsTopUsersController',
                    controllerAs: 'vm'
                }
            }
        });
    }

})();
