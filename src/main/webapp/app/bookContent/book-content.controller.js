(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .controller('BorrowedBookContentController', BorrowedBookContentController);

    BorrowedBookContentController.$inject = ['$scope', '$state', 'ParseLinks', 'AlertService', 'BookContent', 'entities'];

    function BorrowedBookContentController ($scope, $state, ParseLinks, AlertService, BookContent, entities) {
        var vm = this;
        vm.content = entities;
        }
})();
