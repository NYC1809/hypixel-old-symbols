# hypixel-old-symbols

Client-side Fabric mod for reverting Hypixel's newer custom symbol characters back to older ASCII-style replacements.

## How it works

This mod rewrites matching characters at two layers:

1. `PlainTextContent.Literal` — affects most `Text` objects before other mods call APIs like `getString()`.
2. `TextVisitFactory` — affects rendering/measurement and other text visitor paths.

That gives you much better coverage than a font-only resource pack, because many mods will now see the replaced characters when they read `Text`.

## Important limitation

No client mod can force **every** other mod to see replaced text in all cases. Another mod can still bypass this by:

- reading raw packet fields before your code touches them,
- inspecting non-text data directly,
- or using custom packet handlers / custom UI data.

If you want the widest compatibility, rewrite the text as early as possible (which this mod does for Minecraft's normal `Text` pipeline) and expose a shared replacement helper for any cooperating mods.

## Configuration

On first launch, the mod creates:

`config/hypixel-old-symbols.json`

Example:

```json
{
  "enabled": true,
  "replacements": {
    "\uE000": "*",
    "\uE001": "+"
  }
}
```

Replace the `\uE000`/`\uE001` keys with the actual Hypixel custom characters you want to map.

## Build

```powershell
.\gradlew.bat build
```

