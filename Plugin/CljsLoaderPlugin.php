<?php

declare(strict_types=1);

namespace Example\GiftList\Plugin;

use Magento\Framework\Module\Dir\Reader as ModuleDirReader;
use Magento\Framework\View\Page\Config\Renderer;

class CljsLoaderPlugin
{
    private $scriptPath = 'js/giftlist.js';

    /**
     * @var ModuleDirReader
     */
    private $moduleDirReader;

    public function __construct(ModuleDirReader $moduleDirReader)
    {
        $this->moduleDirReader = $moduleDirReader;
    }

    public function afterRenderAssets(Renderer $subject, string $result)
    {
        return file_exists($this->getFilesystemPath()) ?
            $this->getScriptLink() . $result :
            $result;
    }

    private function getFilesystemPath(): string
    {
        return $this->moduleDirReader->getModuleDir('view', 'Example_GiftList') . '/frontend/web/' . $this->scriptPath;
    }

    private function getFrontendPath(): string
    {
        return '/static/frontend/Magento/blank/cljs/Example_GiftList/' . $this->scriptPath;
    }

    private function getScriptLink(): string
    {
        return sprintf('<script src="%s"></script>', $this->getFrontendPath());
    }
}
